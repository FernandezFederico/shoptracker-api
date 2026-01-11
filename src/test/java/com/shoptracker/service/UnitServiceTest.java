package com.shoptracker.service;

import com.shoptracker.mapper.UnitMapper;
import com.shoptracker.model.dto.UnitResponseDTO;
import com.shoptracker.model.entity.Unit;
import com.shoptracker.repository.UnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("findAll() - UnitService.findAll - Happy Path")
public class UnitServiceTest {
    //findAll -> Happi Path
    //Mock dependencias -> UnitRepository | InjectMock -> UnitService
    // Estructura del test AAA

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private UnitMapper unitMapper;

    @InjectMocks
    private UnitService unitService;

    @Test
    void findAll_shouldReturnAllUnits() {
        // Arrange
        Unit unit1 = new Unit(1L, "Kilogram", "kg");
        Unit unit2 = new Unit(2L, "Liter", "l");

        UnitResponseDTO dto1 = new UnitResponseDTO(1L, "Kilogram", "kg");
        UnitResponseDTO dto2 = new UnitResponseDTO(2L, "Liter", "l");

        when(unitRepository.findAll()).thenReturn(List.of(unit1, unit2));
        when(unitMapper.toResponseDTO(unit1)).thenReturn(dto1);
        when(unitMapper.toResponseDTO(unit2)).thenReturn(dto2);

        // Act
        List<UnitResponseDTO> result = unitService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Kilogram", result.get(0).getName());
        assertEquals("kg", result.get(0).getAbbreviation());

        verify(unitRepository).findAll();
        verify(unitMapper).toResponseDTO(unit1);
        verify(unitMapper).toResponseDTO(unit2);
    }
}