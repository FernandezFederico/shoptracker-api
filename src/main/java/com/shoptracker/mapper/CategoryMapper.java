package com.shoptracker.mapper;

import com.shoptracker.model.dto.CategoryRequestDTO;
import com.shoptracker.model.dto.CategoryResponseDTO;
import com.shoptracker.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    /**
     * Convierte RequestDTO → Entity
     * Se usa cuando el cliente CREA una categoría
     */
    public Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Category entity = new Category();
        entity.setName(dto.getName());
        entity.setIcon(dto.getIcon());
        // NO seteamos el ID porque lo genera la DB

        return entity;
    }

    /**
     * Convierte Entity → ResponseDTO
     * Se usa cuando devolvemos datos al cliente
     */
    public CategoryResponseDTO toResponseDTO(Category entity) {
        if (entity == null) {
            return null;
        }

        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());

        return dto;
    }

    /**
     * Actualiza una Entity existente con datos del RequestDTO
     * Se usa en el UPDATE (PUT)
     */
    public void updateEntityFromDTO(CategoryRequestDTO dto, Category entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setIcon(dto.getIcon());
        // NO tocamos el ID (debe mantenerse)
    }
}