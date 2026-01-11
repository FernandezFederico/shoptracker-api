package com.shoptracker.controller;

import com.shoptracker.model.dto.UnitResponseDTO;
import com.shoptracker.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    public ResponseEntity<List<UnitResponseDTO>> getAll() {
        List<UnitResponseDTO> units = unitService.findAll();
        return ResponseEntity.ok(units);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitResponseDTO> getById(@PathVariable Long id) {
        UnitResponseDTO unit = unitService.findById(id);
        return ResponseEntity.ok(unit);
    }
}
