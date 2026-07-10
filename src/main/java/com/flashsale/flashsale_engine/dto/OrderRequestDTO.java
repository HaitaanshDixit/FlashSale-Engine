package com.flashsale.flashsale_engine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotNull(message = "Sneaker ID cannot be null")
    private Long sneakerId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}