package com.flashsale.flashsale_engine.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SneakerResponseDTO {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer totalStock;
    private Integer flashSaleStock;
    private String imageUrl;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
    private LocalDateTime createdAt;
}