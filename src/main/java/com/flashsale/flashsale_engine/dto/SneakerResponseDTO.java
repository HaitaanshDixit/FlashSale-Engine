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
    private String imageUrl;

    //creates urgency ("Only 5 left!")
    private Integer flashSaleStock;

    // Frontend uses these to render countdown timers
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;

    // Tells frontend exactly what state the sale is in
    private String saleStatus; // UPCOMING / ACTIVE / SOLD_OUT / ENDED

    private Boolean isSoldOut;
}