package com.shoptracker.mapper;

import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.model.dto.ProductRequestDTO;
import com.shoptracker.model.dto.ProductResponseDTO;
import com.shoptracker.model.entity.Category;
import com.shoptracker.model.entity.Product;
import com.shoptracker.model.entity.Unit;
import com.shoptracker.repository.CategoryRepository;
import com.shoptracker.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra la categoría con id: " + dto.getCategoryId()));

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra la unidad con id: " + dto.getUnitId()));  // ← Corregido

        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setCategory(category);
        entity.setUnit(unit);

        return entity;
    }

    public ProductResponseDTO toResponseDTO(Product entity) {
        if (entity == null) {
            return null;
        }

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setUnitId(entity.getUnit().getId());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public void updateEntityFromDTO(ProductRequestDTO dto, Product entity) {
        if (dto == null || entity == null) {
            return;
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra la categoría con id: " + dto.getCategoryId()));

        Unit unit = unitRepository.findById(dto.getUnitId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra la unidad con id: " + dto.getUnitId()));  // ← Corregido

        entity.setName(dto.getName());
        entity.setCategory(category);
        entity.setUnit(unit);
    }
}