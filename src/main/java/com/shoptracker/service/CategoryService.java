package com.shoptracker.service;

import com.shoptracker.exception.DuplicateResourceException;
import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.CategoryMapper;
import com.shoptracker.model.dto.CategoryRequestDTO;
import com.shoptracker.model.dto.CategoryResponseDTO;
import com.shoptracker.model.entity.Category;
import com.shoptracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryResponseDTO create(CategoryRequestDTO requestDTO) {
        if (categoryRepository.existsByName(requestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Ya existe una categoría con el nombre: " + requestDTO.getName());
        }

        Category entity = categoryMapper.toEntity(requestDTO);
        Category savedEntity = categoryRepository.save(entity);
        return categoryMapper.toResponseDTO(savedEntity);
    }

    public List<CategoryResponseDTO> findAll() {
        List<Category> entities = categoryRepository.findAll();
        return entities.stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoryResponseDTO findById(Long id) {
        Category entity = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + id));

        return categoryMapper.toResponseDTO(entity);
    }

    public CategoryResponseDTO update(Long id, CategoryRequestDTO requestDTO) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría no encontrada con id: " + id));

        if (!existing.getName().equals(requestDTO.getName())) {
            if (categoryRepository.existsByName(requestDTO.getName())) {
                throw new DuplicateResourceException(
                        "Ya existe una categoría con el nombre: " + requestDTO.getName());
            }
        }

        categoryMapper.updateEntityFromDTO(requestDTO, existing);
        Category updatedEntity = categoryRepository.save(existing);
        return categoryMapper.toResponseDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Categoría no encontrada con id: " + id);
        }

        categoryRepository.deleteById(id);
    }
}