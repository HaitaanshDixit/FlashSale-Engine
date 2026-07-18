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
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {

        //1: Find the sneaker
        Sneaker sneaker = sneakerRepository.findByIdWithPessimisticLock(requestDTO.getSneakerId()).orElseThrow(() -> new ResourceNotFoundException("Sneaker not found with id: " + requestDTO.getSneakerId()));

        //2: Check if sale is active
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (sneaker.getSaleStartTime() == null || sneaker.getSaleEndTime() == null) {
            throw new RuntimeException("This sneaker has no active flash sale");
        }
        if (now.isBefore(sneaker.getSaleStartTime())) {
            throw new RuntimeException("Flash sale has not started yet");
        }
        if (now.isAfter(sneaker.getSaleEndTime())) {
            throw new RuntimeException("Flash sale has ended");
        }

        //3: Check if already ordered by this user
        boolean alreadyOrdered = orderRepository.existsBySneakerIdAndUserId(requestDTO.getSneakerId(), requestDTO.getUserId());
        if (alreadyOrdered) {
            throw new RuntimeException("You have already purchased this sneaker");
        }

        //4: Check stock
        if (sneaker.getFlashSaleStock() <= 0) {
            throw new RuntimeException("Sneaker is sold out");
        }

        // Step 5: Decrement stock — THIS IS THE NAIVE PART
        // No locking here — will break under concurrent load
        sneaker.setFlashSaleStock(sneaker.getFlashSaleStock() - 1);
        sneakerRepository.save(sneaker);

        //6: Create order
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