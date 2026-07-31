package com.flashsale.flashsale_engine.controller;

import com.flashsale.flashsale_engine.dto.AuthResponseDTO;
import com.flashsale.flashsale_engine.dto.LoginRequestDTO;
import com.flashsale.flashsale_engine.dto.RegisterRequestDTO;
import com.flashsale.flashsale_engine.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return ResponseEntity.ok(authService.login(requestDTO));
    }
}