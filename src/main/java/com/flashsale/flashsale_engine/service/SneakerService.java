package com.flashsale.flashsale_engine.service;

import com.flashsale.flashsale_engine.dto.SneakerRequestDTO;
import com.flashsale.flashsale_engine.dto.SneakerResponseDTO;
import com.flashsale.flashsale_engine.dto.SneakerStaticDTO;
import com.flashsale.flashsale_engine.exception.ResourceNotFoundException;
import com.flashsale.flashsale_engine.model.Sneaker;
import com.flashsale.flashsale_engine.repository.SneakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SneakerService {

    private final SneakerRepository sneakerRepository;
    private final RedisStockService redisStockService;

    // PUBLIC ENTRY POINTS (uncached, always merge live stock)
    public List<SneakerResponseDTO> getAllSneakers() {
        return getAllStaticData().stream().map(this::mergeWithLiveStock).collect(Collectors.toList());
    }

    public SneakerResponseDTO getSneakerById(Long id) {
        SneakerStaticDTO staticData = getStaticDataById(id);
        return mergeWithLiveStock(staticData);
    }

    // CACHED STATIC DATA (name/brand/price/image/sale window)
    @Cacheable(value = "sneakers", key = "'all'")
    public List<SneakerStaticDTO> getAllStaticData() {
        System.out.println("Fetching sneaker STATIC data from DB (cache miss)");
        return sneakerRepository.findAll().stream().map(this::mapToStaticDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "sneakers", key = "#id")
    public SneakerStaticDTO getStaticDataById(Long id) {
        System.out.println("Fetching sneaker " + id + " STATIC data from DB (cache miss)");
        Sneaker sneaker = sneakerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + id));
        return mapToStaticDTO(sneaker);
    }

    //WRITE OPERATIONS (evict the static cache)
    @CacheEvict(value = "sneakers", key = "'all'")
    public SneakerResponseDTO createSneaker(SneakerRequestDTO requestDTO) {
        Sneaker sneaker = mapToEntity(requestDTO);
        Sneaker saved = sneakerRepository.save(sneaker);
        redisStockService.initializeStock(saved.getId(), saved.getFlashSaleStock());
        return mergeWithLiveStock(mapToStaticDTO(saved));
    }

    @Caching(evict = {
            @CacheEvict(value = "sneakers", key = "'all'"),
            @CacheEvict(value = "sneakers", key = "#id")
    })
    public SneakerResponseDTO updateSneaker(Long id, SneakerRequestDTO requestDTO) {
        Sneaker sneaker = sneakerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + id));
        sneaker.setName(requestDTO.getName());
        sneaker.setBrand(requestDTO.getBrand());
        sneaker.setPrice(requestDTO.getPrice());
        sneaker.setTotalStock(requestDTO.getTotalStock());
        sneaker.setFlashSaleStock(requestDTO.getFlashSaleStock());
        sneaker.setImageUrl(requestDTO.getImageUrl());
        sneaker.setSaleStartTime(requestDTO.getSaleStartTime());
        sneaker.setSaleEndTime(requestDTO.getSaleEndTime());

        Sneaker updated = sneakerRepository.save(sneaker);
        // Admin explicitly changed stock so then resync the live Redis counter to match.
        redisStockService.initializeStock(updated.getId(), updated.getFlashSaleStock());
        return mergeWithLiveStock(mapToStaticDTO(updated));
    }

    @Caching(evict = {
            @CacheEvict(value = "sneakers", key = "'all'"),
            @CacheEvict(value = "sneakers", key = "#id")
    })
    public void deleteSneaker(Long id) {
        if (!sneakerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sneaker not found with id: " + id);
        }
        sneakerRepository.deleteById(id);
    }


    private SneakerResponseDTO mergeWithLiveStock(SneakerStaticDTO staticData) {
        Long liveStock = redisStockService.getStock(staticData.getId());

        // Defensive fallback: if Redis has no counter for this id (e.g. Redis was
        // flushed without an app restart), fall back to Postgres and re-seed Redis
        // so this doesn't repeat on the next request.
        if (liveStock == null) {
            Sneaker sneaker = sneakerRepository.findById(staticData.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + staticData.getId()));
            redisStockService.initializeStock(sneaker.getId(), sneaker.getFlashSaleStock());
            liveStock = (long) sneaker.getFlashSaleStock();
        }

        int stock = liveStock.intValue();

        return SneakerResponseDTO.builder()
                .id(staticData.getId())
                .name(staticData.getName())
                .brand(staticData.getBrand())
                .price(staticData.getPrice())
                .imageUrl(staticData.getImageUrl())
                .flashSaleStock(stock)
                .saleStartTime(staticData.getSaleStartTime())
                .saleEndTime(staticData.getSaleEndTime())
                .saleStatus(computeSaleStatus(staticData.getSaleStartTime(), staticData.getSaleEndTime(), stock))
                .isSoldOut(stock <= 0)
                .build();
    }

    private SneakerStaticDTO mapToStaticDTO(Sneaker sneaker) {
        return SneakerStaticDTO.builder()
                .id(sneaker.getId())
                .name(sneaker.getName())
                .brand(sneaker.getBrand())
                .price(sneaker.getPrice())
                .imageUrl(sneaker.getImageUrl())
                .saleStartTime(sneaker.getSaleStartTime())
                .saleEndTime(sneaker.getSaleEndTime())
                .build();
    }

    private Sneaker mapToEntity(SneakerRequestDTO dto) {
        return Sneaker.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .totalStock(dto.getTotalStock())
                .flashSaleStock(dto.getFlashSaleStock())
                .imageUrl(dto.getImageUrl())
                .saleStartTime(dto.getSaleStartTime())
                .saleEndTime(dto.getSaleEndTime())
                .build();
    }

    private String computeSaleStatus(LocalDateTime saleStart, LocalDateTime saleEnd, int stock) {
        LocalDateTime now = LocalDateTime.now();

        if (saleStart == null || saleEnd == null) {
            return "NO_SALE";
        }
        if (stock <= 0) {
            return "SOLD_OUT";
        }
        if (now.isBefore(saleStart)) {
            return "UPCOMING";
        }
        if (now.isAfter(saleEnd)) {
            return "ENDED";
        }
        return "ACTIVE";
    }
}