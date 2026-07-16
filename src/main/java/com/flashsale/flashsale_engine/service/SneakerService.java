package com.flashsale.flashsale_engine.service;

import com.flashsale.flashsale_engine.dto.SneakerRequestDTO;
import com.flashsale.flashsale_engine.dto.SneakerResponseDTO;
import com.flashsale.flashsale_engine.exception.ResourceNotFoundException;
import com.flashsale.flashsale_engine.model.Sneaker;
import com.flashsale.flashsale_engine.repository.SneakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//Lombok's annotation. Since sneakerRepository is declared as final, Lombok automatically generates a constructor that injects it.
public class SneakerService {

    private final SneakerRepository sneakerRepository;

    // creating
    public SneakerResponseDTO createSneaker(SneakerRequestDTO requestDTO) {
        Sneaker sneaker = mapToEntity(requestDTO);
        Sneaker saved = sneakerRepository.save(sneaker);
        return mapToResponseDTO(saved);
    }

    // getting all the sneakers
    public List<SneakerResponseDTO> getAllSneakers() {
        return sneakerRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // get by id
    public SneakerResponseDTO getSneakerById(Long id) {
        Sneaker sneaker = sneakerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + id));
        return mapToResponseDTO(sneaker);
    }

    // updating any sneaker args
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
        return mapToResponseDTO(updated);
    }

    // deleting data of our product by id
    public void deleteSneaker(Long id) {
        if (!sneakerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sneaker not found with id: " + id);
        }
        sneakerRepository.deleteById(id);
    }


    private SneakerResponseDTO mapToResponseDTO(Sneaker sneaker) {
        return SneakerResponseDTO.builder()
                .id(sneaker.getId())
                .name(sneaker.getName())
                .brand(sneaker.getBrand())
                .price(sneaker.getPrice())
                .imageUrl(sneaker.getImageUrl())
                .flashSaleStock(sneaker.getFlashSaleStock())
                .saleStartTime(sneaker.getSaleStartTime())
                .saleEndTime(sneaker.getSaleEndTime())
                .saleStatus(computeSaleStatus(sneaker))
                .isSoldOut(sneaker.getFlashSaleStock() == 0)
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

    // HELPER : Compute sale status
    private String computeSaleStatus(Sneaker sneaker) {
        LocalDateTime now = LocalDateTime.now();

        if (sneaker.getSaleStartTime() == null || sneaker.getSaleEndTime() == null) {
            return "NO_SALE";
        }
        if (sneaker.getFlashSaleStock() == 0) {
            return "SOLD_OUT";
        }
        if (now.isBefore(sneaker.getSaleStartTime())) {
            return "UPCOMING";
        }
        if (now.isAfter(sneaker.getSaleEndTime())) {
            return "ENDED";
        }
        return "ACTIVE";
    }
}