package com.flashsale.flashsale_engine.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDTO {
    private String token;
    private Long userId;
    private String name;
    private String email;
}