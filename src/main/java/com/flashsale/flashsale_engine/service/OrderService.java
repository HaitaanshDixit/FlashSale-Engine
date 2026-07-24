package com.flashsale.flashsale_engine.service;

import com.flashsale.flashsale_engine.dto.OrderRequestDTO;
import com.flashsale.flashsale_engine.dto.OrderResponseDTO;
import com.flashsale.flashsale_engine.exception.ResourceNotFoundException;
import com.flashsale.flashsale_engine.model.Order;
import com.flashsale.flashsale_engine.model.OrderStatus;
import com.flashsale.flashsale_engine.model.Sneaker;
import com.flashsale.flashsale_engine.repository.OrderRepository;
import com.flashsale.flashsale_engine.repository.SneakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final SneakerRepository sneakerRepository;
    private final RedisStockService redisStockService;

    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {

        // Step 1: Cheap, non-locking fetch just to check sale window and get flashSaleStock for Redis init
        Sneaker sneakerCheck = sneakerRepository.findById(requestDTO.getSneakerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + requestDTO.getSneakerId()));

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (sneakerCheck.getSaleStartTime() == null || sneakerCheck.getSaleEndTime() == null) {
            throw new RuntimeException("This sneaker has no active flash sale");
        }
        if (now.isBefore(sneakerCheck.getSaleStartTime())) {
            throw new RuntimeException("Flash sale has not started yet");
        }
        if (now.isAfter(sneakerCheck.getSaleEndTime())) {
            throw new RuntimeException("Flash sale has ended");
        }

        // Step 2: Check if already ordered (no lock needed, just a read)
        boolean alreadyOrdered = orderRepository.existsBySneakerIdAndUserId(requestDTO.getSneakerId(), requestDTO.getUserId());
        if (alreadyOrdered) {
            throw new RuntimeException("You have already purchased this sneaker");
        }

        // Step 3: REDIS SPEED GATE — reject here, before ever touching the pessimistic lock
        if (!redisStockService.isStockInitialized(requestDTO.getSneakerId())) {
            redisStockService.initializeStock(requestDTO.getSneakerId(), sneakerCheck.getFlashSaleStock());
        }

        Long remainingStock = redisStockService.decrementStock(requestDTO.getSneakerId());
        if (remainingStock < 0) {
            redisStockService.incrementStock(requestDTO.getSneakerId()); // restore over-decrement
            throw new RuntimeException("Sneaker is sold out");
        }

        // Step 4: Only requests that passed the Redis gate reach the pessimistic lock
        Sneaker sneaker = sneakerRepository.findByIdWithPessimisticLock(requestDTO.getSneakerId())
                .orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + requestDTO.getSneakerId()));

        // Step 5: DB stock double safety net
        if (sneaker.getFlashSaleStock() <= 0) {
            redisStockService.incrementStock(requestDTO.getSneakerId());
            throw new RuntimeException("Sneaker is sold out");
        }

        // Step 6: Decrement DB stock
        sneaker.setFlashSaleStock(sneaker.getFlashSaleStock() - 1);
        sneakerRepository.save(sneaker);

        // Step 7: Create order
        Order order = Order.builder()
                .sneaker(sneaker)
                .userId(requestDTO.getUserId())
                .quantity(1)
                .priceAtPurchase(sneaker.getPrice())
                .status(OrderStatus.PENDING)
                .build();

        Order saved = orderRepository.save(order);
        return mapToResponseDTO(saved);
    }

    //GET ALL ORDERS BY USER
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    //GET ORDER BY ID
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order not found with id: " + orderId));
        return mapToResponseDTO(order);
    }
    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .sneakerId(order.getSneaker().getId())
                .sneakerName(order.getSneaker().getName())
                .sneakerBrand(order.getSneaker().getBrand())
                .userId(order.getUserId())
                .quantity(order.getQuantity())
                .priceAtPurchase(order.getPriceAtPurchase())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}