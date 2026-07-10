package com.flashsale.flashsale_engine.dto;

import com.flashsale.flashsale_engine.model.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long orderId;
    private Long sneakerId;
    private String sneakerName;
    private String sneakerBrand;
    private Long userId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private OrderStatus status;
    private LocalDateTime createdAt;
}