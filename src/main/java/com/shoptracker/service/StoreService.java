package com.shoptracker.service;

import com.shoptracker.exception.DuplicateResourceException;
import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.StoreMapper;
import com.shoptracker.model.dto.StoreListResponseDTO;
import com.shoptracker.model.dto.StoreRequestDTO;
import com.shoptracker.model.dto.StoreResponseDTO;
import com.shoptracker.model.entity.Store;
import com.shoptracker.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public StoreResponseDTO create(StoreRequestDTO requestDTO) {
        if (storeRepository.existsByName(requestDTO.getName())) {
            throw new DuplicateResourceException(
                    "Ya existe una tienda con el nombre: " + requestDTO.getName());
        }

        Store entity = storeMapper.toEntity(requestDTO);
        Store savedEntity = storeRepository.save(entity);
        return storeMapper.toResponseDTO(savedEntity);
    }

    public StoreListResponseDTO findAll(int page, int pageSize) {
        // Crear objeto Pageable (página y tamaño)
        Pageable pageable = PageRequest.of(page - 1, pageSize);  // page - 1 porque Spring usa base 0

        // Obtener página de resultados
        Page<Store> storePage = storeRepository.findAll(pageable);

        // Convertir entities a DTOs
        List<StoreResponseDTO> storeDTOs = storePage.getContent()
                .stream()
                .map(storeMapper::toResponseDTO)
                .collect(Collectors.toList());

        // Construir respuesta paginada
        return new StoreListResponseDTO(
                storeDTOs,
                storePage.getTotalElements(),  // Total de registros
                page,
                pageSize
        );
    }

    public StoreResponseDTO findById(Long id) {
        Store entity = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tienda no encontrada con id: " + id));

        return storeMapper.toResponseDTO(entity);
    }

    public StoreResponseDTO update(Long id, StoreRequestDTO requestDTO) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tienda no encontrada con id: " + id));

        // Si cambia el nombre, verificar que no esté duplicado
        if (!existing.getName().equals(requestDTO.getName())) {
            if (storeRepository.existsByName(requestDTO.getName())) {
                throw new DuplicateResourceException(
                        "Ya existe una tienda con el nombre: " + requestDTO.getName());
            }
        }

        storeMapper.updateEntityFromDTO(requestDTO, existing);
        Store updatedEntity = storeRepository.save(existing);
        return storeMapper.toResponseDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        if (!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Tienda no encontrada con id: " + id);
        }
        storeRepository.deleteById(id);
    }
}