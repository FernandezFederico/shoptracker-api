package com.shoptracker.mapper;

import com.shoptracker.model.dto.UnitResponseDTO;
import com.shoptracker.model.entity.Unit;
import org.springframework.stereotype.Component;

@Component
public class UnitMapper {
    public UnitResponseDTO toResponseDTO(Unit entity){
        if (entity == null){
            return null;
        }

        UnitResponseDTO dto = new UnitResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAbbreviation(entity.getAbbreviation());

        return dto;
    }


}
