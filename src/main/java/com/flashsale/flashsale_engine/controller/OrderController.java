package com.flashsale.flashsale_engine.controller;

import com.flashsale.flashsale_engine.dto.OrderRequestDTO;
import com.flashsale.flashsale_engine.dto.OrderResponseDTO;
import com.flashsale.flashsale_engine.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // place order
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO response = orderService.placeOrder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get order by id (ownership check happens in the service layer)
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // get all orders for the AUTHENTICATED user
    // The user is now derived entirely from the verified JWT inside the service.
    @GetMapping("/user")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser() {
        List<OrderResponseDTO> orders = orderService.getOrdersByUser();
        return ResponseEntity.ok(orders);
    }
}