    package com.shoptracker.controller;

import com.shoptracker.model.dto.CategoryRequestDTO;
import com.shoptracker.model.dto.CategoryResponseDTO;
import com.shoptracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * POST /v1/categories
     * Crear una nueva categoría
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO response = categoryService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /v1/categories
     * Obtener todas las categorías
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        List<CategoryResponseDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * GET /v1/categories/{id}
     * Obtener una categoría por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getById(@PathVariable Long id) {
        CategoryResponseDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * PUT /v1/categories/{id}
     * Actualizar una categoría existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryResponseDTO response = categoryService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /v1/categories/{id}
     * Eliminar una categoría
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}