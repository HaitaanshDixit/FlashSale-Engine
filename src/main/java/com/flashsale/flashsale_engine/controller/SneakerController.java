package com.flashsale.flashsale_engine.controller;

import com.flashsale.flashsale_engine.dto.SneakerRequestDTO;
import com.flashsale.flashsale_engine.dto.SneakerResponseDTO;
import com.flashsale.flashsale_engine.service.SneakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sneakers")  // base URL for all endpoints in this controller
@RequiredArgsConstructor
public class SneakerController {

    private final SneakerService sneakerService;

    // @RequestBody SneakerRequestDTO requestDTO : tells Spring to take the JSON from the request body and automatically convert it into a SneakerRequestDTO object
    // @Valid tells Spring "run the validation checks on this object before proceeding.
    @PostMapping
    public ResponseEntity<SneakerResponseDTO> createSneaker(@Valid @RequestBody SneakerRequestDTO requestDTO) {
        SneakerResponseDTO response = sneakerService.createSneaker(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<List<SneakerResponseDTO>> getAllSneakers() {
        List<SneakerResponseDTO> sneakers = sneakerService.getAllSneakers();
        return ResponseEntity.ok(sneakers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SneakerResponseDTO> getSneakerById(@PathVariable Long id) {
        SneakerResponseDTO sneaker = sneakerService.getSneakerById(id);
        return ResponseEntity.ok(sneaker);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SneakerResponseDTO> updateSneaker(@PathVariable Long id, @Valid @RequestBody SneakerRequestDTO requestDTO) {
        SneakerResponseDTO updated = sneakerService.updateSneaker(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSneaker(@PathVariable Long id) {
        sneakerService.deleteSneaker(id);
        return ResponseEntity.ok("Sneaker deleted successfully");
    }
}