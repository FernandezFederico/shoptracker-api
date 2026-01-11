package com.shoptracker.service;

import com.shoptracker.exception.DuplicateResourceException;
import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.CategoryMapper;
import com.shoptracker.model.dto.CategoryRequestDTO;
import com.shoptracker.model.dto.CategoryResponseDTO;
import com.shoptracker.model.entity.Category;
import com.shoptracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryService Tests")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryRequestDTO validRequestDTO;
    private Category validCategory;
    private CategoryResponseDTO validResponseDTO;

    @BeforeEach
    void setUp() {
        // Datos de prueba reutilizables
        validRequestDTO = new CategoryRequestDTO();
        validRequestDTO.setName("Alimentos");
        validRequestDTO.setIcon("food");

        validCategory = new Category();
        validCategory.setId(1L);
        validCategory.setName("Alimentos");
        validCategory.setIcon("food");

        validResponseDTO = new CategoryResponseDTO();
        validResponseDTO.setId(1L);
        validResponseDTO.setName("Alimentos");
        validResponseDTO.setIcon("food");
    }

    // ============================================
    // TESTS DEL MÉTODO CREATE
    // ============================================

    @Test
    @DisplayName("create() - Debe crear categoría exitosamente")
    void create_shouldCreateCategorySuccessfully() {
        // ARRANGE
        when(categoryRepository.existsByName("Alimentos")).thenReturn(false);
        when(categoryMapper.toEntity(validRequestDTO)).thenReturn(validCategory);
        when(categoryRepository.save(any(Category.class))).thenReturn(validCategory);
        when(categoryMapper.toResponseDTO(validCategory)).thenReturn(validResponseDTO);

        // ACT
        CategoryResponseDTO result = categoryService.create(validRequestDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alimentos", result.getName());
        assertEquals("food", result.getIcon());

        // Verificar interacciones
        verify(categoryRepository).existsByName("Alimentos");
        verify(categoryMapper).toEntity(validRequestDTO);
        verify(categoryRepository).save(any(Category.class));
        verify(categoryMapper).toResponseDTO(validCategory);
    }

    @Test
    @DisplayName("create() - Debe lanzar excepción cuando nombre ya existe")
    void create_shouldThrowException_whenNameAlreadyExists() {
        // ARRANGE
        when(categoryRepository.existsByName("Alimentos")).thenReturn(true);

        // ACT & ASSERT
        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> categoryService.create(validRequestDTO)
        );

        assertTrue(exception.getMessage().contains("Alimentos"));

        // Verificar que NO se guardó nada
        verify(categoryRepository).existsByName("Alimentos");
        verify(categoryRepository, never()).save(any());
        verify(categoryMapper, never()).toEntity(any());
    }

    // ============================================
    // TESTS DEL MÉTODO FINDALL
    // ============================================

    @Test
    @DisplayName("findAll() - Debe devolver lista de categorías")
    void findAll_shouldReturnListOfCategories() {
        // ARRANGE
        Category category1 = new Category(1L, "Alimentos", "food");
        Category category2 = new Category(2L, "Limpieza", "clean");
        List<Category> categories = Arrays.asList(category1, category2);

        CategoryResponseDTO dto1 = new CategoryResponseDTO(1L, "Alimentos", "food");
        CategoryResponseDTO dto2 = new CategoryResponseDTO(2L, "Limpieza", "clean");

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toResponseDTO(category1)).thenReturn(dto1);
        when(categoryMapper.toResponseDTO(category2)).thenReturn(dto2);

        // ACT
        List<CategoryResponseDTO> result = categoryService.findAll();

        // ASSERT
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alimentos", result.get(0).getName());
        assertEquals("Limpieza", result.get(1).getName());

        verify(categoryRepository).findAll();
        verify(categoryMapper, times(2)).toResponseDTO(any());
    }

    @Test
    @DisplayName("findAll() - Debe devolver lista vacía cuando no hay categorías")
    void findAll_shouldReturnEmptyList_whenNoCategoriesExist() {
        // ARRANGE
        when(categoryRepository.findAll()).thenReturn(Arrays.asList());

        // ACT
        List<CategoryResponseDTO> result = categoryService.findAll();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll();
    }

    // ============================================
    // TESTS DEL MÉTODO FINDBYID
    // ============================================

    @Test
    @DisplayName("findById() - Debe devolver categoría cuando existe")
    void findById_shouldReturnCategory_whenExists() {
        // ARRANGE
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(validCategory));
        when(categoryMapper.toResponseDTO(validCategory)).thenReturn(validResponseDTO);

        // ACT
        CategoryResponseDTO result = categoryService.findById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alimentos", result.getName());

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toResponseDTO(validCategory);
    }

    @Test
    @DisplayName("findById() - Debe lanzar excepción cuando no existe")
    void findById_shouldThrowException_whenNotExists() {
        // ARRANGE
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));

        verify(categoryRepository).findById(999L);
        verify(categoryMapper, never()).toResponseDTO(any());
    }

    // ============================================
    // TESTS DEL MÉTODO UPDATE
    // ============================================

    @Test
    @DisplayName("update() - Debe actualizar categoría exitosamente")
    void update_shouldUpdateCategorySuccessfully() {
        // ARRANGE
        CategoryRequestDTO updateDTO = new CategoryRequestDTO();
        updateDTO.setName("Alimentos Modificado");
        updateDTO.setIcon("food-new");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Alimentos Modificado");
        updatedCategory.setIcon("food-new");

        CategoryResponseDTO updatedResponseDTO = new CategoryResponseDTO();
        updatedResponseDTO.setId(1L);
        updatedResponseDTO.setName("Alimentos Modificado");
        updatedResponseDTO.setIcon("food-new");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(validCategory));
        when(categoryRepository.existsByName("Alimentos Modificado")).thenReturn(false);
        when(categoryRepository.save(validCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toResponseDTO(updatedCategory)).thenReturn(updatedResponseDTO);

        // ACT
        CategoryResponseDTO result = categoryService.update(1L, updateDTO);

        // ASSERT
        assertNotNull(result);
        assertEquals("Alimentos Modificado", result.getName());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).existsByName("Alimentos Modificado");
        verify(categoryMapper).updateEntityFromDTO(updateDTO, validCategory);
        verify(categoryRepository).save(validCategory);
    }

    @Test
    @DisplayName("update() - Debe lanzar excepción cuando nombre duplicado")
    void update_shouldThrowException_whenNameDuplicate() {
        // ARRANGE
        CategoryRequestDTO updateDTO = new CategoryRequestDTO();
        updateDTO.setName("Limpieza"); // Ya existe

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(validCategory));
        when(categoryRepository.existsByName("Limpieza")).thenReturn(true);

        // ACT & ASSERT
        assertThrows(
                DuplicateResourceException.class,
                () -> categoryService.update(1L, updateDTO)
        );

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).existsByName("Limpieza");
        verify(categoryRepository, never()).save(any());
    }

    // ============================================
    // TESTS DEL MÉTODO DELETEBYID
    // ============================================

    @Test
    @DisplayName("deleteById() - Debe eliminar categoría cuando existe")
    void deleteById_shouldDeleteCategory_whenExists() {
        // ARRANGE
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // ACT
        categoryService.deleteById(1L);

        // ASSERT
        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById() - Debe lanzar excepción cuando no existe")
    void deleteById_shouldThrowException_whenNotExists() {
        // ARRANGE
        when(categoryRepository.existsById(999L)).thenReturn(false);

        // ACT & ASSERT
        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deleteById(999L)
        );

        verify(categoryRepository).existsById(999L);
        verify(categoryRepository, never()).deleteById(any());
    }
}