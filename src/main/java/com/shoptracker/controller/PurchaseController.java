package com.shoptracker.controller;

import com.shoptracker.model.dto.PurchaseListResponseDTO;
import com.shoptracker.model.dto.PurchaseRequestDTO;
import com.shoptracker.model.dto.PurchaseResponseDTO;
import com.shoptracker.model.dto.PurchaseSummaryResponseDTO;
import com.shoptracker.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> create(
            @Valid @RequestBody PurchaseRequestDTO requestDTO
            ) {
        PurchaseResponseDTO response = purchaseService.create(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PurchaseListResponseDTO> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        PurchaseListResponseDTO response = purchaseService.findAll(page, pageSize);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> getById(@PathVariable Long id) {
        PurchaseResponseDTO response = purchaseService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequestDTO requestDTO){
        PurchaseResponseDTO response = purchaseService.update(id, requestDTO);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        purchaseService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public ResponseEntity<PurchaseSummaryResponseDTO> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {

        PurchaseSummaryResponseDTO summary = purchaseService.getSummary(dateFrom, dateTo);
        return ResponseEntity.ok(summary);
    }
}
