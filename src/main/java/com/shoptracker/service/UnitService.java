package com.shoptracker.service;

import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.UnitMapper;
import com.shoptracker.model.dto.UnitResponseDTO;
import com.shoptracker.model.entity.Unit;
import com.shoptracker.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public List<UnitResponseDTO> findAll() {
        List<Unit> entities = unitRepository.findAll();
        return entities.stream()
                .map(unitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UnitResponseDTO findById(Long id) {
        Unit entity = unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Unidad no encontrada con id " + id));

        return unitMapper.toResponseDTO(entity);
    }
}
