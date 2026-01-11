package com.shoptracker.controller;

import com.shoptracker.model.dto.StoreListResponseDTO;
import com.shoptracker.model.dto.StoreRequestDTO;
import com.shoptracker.model.dto.StoreResponseDTO;
import com.shoptracker.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDTO> create(
            @Valid @RequestBody StoreRequestDTO requestDTO) {
        StoreResponseDTO response = storeService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<StoreListResponseDTO> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        StoreListResponseDTO response = storeService.findAll(page,pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> getById(@PathVariable Long id) {
        StoreResponseDTO store = storeService.findById(id);
        return ResponseEntity.ok(store);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequestDTO requestDTO) {
        StoreResponseDTO response = storeService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        storeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}