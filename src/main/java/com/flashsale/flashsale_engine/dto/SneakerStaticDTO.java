package com.flashsale.flashsale_engine.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Only the slow-changing fields are in here, this is what actually gets cached.
// Stock-related fields (flashSaleStock, isSoldOut, saleStatus) are deliberately excluded: they change on every order, so caching them just means constantly evicting. Instead they're read live and merged in at request time.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SneakerStaticDTO {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String imageUrl;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
}