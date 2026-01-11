package com.shoptracker.service;

import com.shoptracker.exception.DuplicateResourceException;
import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.ProductMapper;
import com.shoptracker.model.dto.ProductListResponseDTO;
import com.shoptracker.model.dto.ProductRequestDTO;
import com.shoptracker.model.dto.ProductResponseDTO;
import com.shoptracker.model.entity.Product;
import com.shoptracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO create(ProductRequestDTO requestDTO){
        if (productRepository.existsByName(requestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Ya existe el producto con el nombre: " + requestDTO.getName());
        }

        Product entity = productMapper.toEntity(requestDTO);
        Product savedEntity = productRepository.save(entity);
        return productMapper.toResponseDTO(savedEntity);
    }

    public ProductListResponseDTO findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponseDTO> productDTOs = productPage.getContent()
                .stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new ProductListResponseDTO(
                productDTOs,
                productPage.getTotalElements(),
                page,
                pageSize
        );
    }

    public ProductResponseDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con el id: " + id));

        return productMapper.toResponseDTO(entity);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO requestDTO) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Producto no encontrado con id: " + id));

        if (!existing.getName().equals(requestDTO.getName())) {
            if (productRepository.existsByName(requestDTO.getName())) {
                throw new DuplicateResourceException(
                        "Ya existe un producto con el nombre : " + requestDTO.getName());
            }
        }

        productMapper.updateEntityFromDTO(requestDTO, existing);
        Product updatedEntity = productRepository.save(existing);
        return productMapper.toResponseDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

}
