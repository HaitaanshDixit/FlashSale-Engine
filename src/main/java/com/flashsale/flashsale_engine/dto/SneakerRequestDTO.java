// DTO = Data Transfer Object.
// It's a simple class that controls what data goes in and out of our API.
//
// Right now our Sneaker entity has fields like createdAt, flashSaleStock, totalStock
// These are internal DB fields. We don't want to expose all of them raw to whoever is calling our API.
//
// For example:
// When someone creates a sneaker (admin), they shouldn't manually set createdAt or id, those are auto-generated
// When someone views a sneaker (user), we control exactly what they see
//
// So we have two DTOs : one for incoming data (request), one for outgoing data (response).

package com.flashsale.flashsale_engine.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SneakerRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Total stock cannot be null")
    @Min(value = 0, message = "Total stock cannot be negative")
    private Integer totalStock;

    @NotNull(message = "Flash sale stock cannot be null")
    @Min(value = 0, message = "Flash sale stock cannot be negative")
    private Integer flashSaleStock;

    private String imageUrl;

    private LocalDateTime saleStartTime;

    private LocalDateTime saleEndTime;
}