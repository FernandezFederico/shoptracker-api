package com.shoptracker.mapper;

import com.shoptracker.model.dto.StoreRequestDTO;
import com.shoptracker.model.dto.StoreResponseDTO;
import com.shoptracker.model.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public Store toEntity(StoreRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Store entity = new Store();
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setIsOnline(dto.getIsOnline() != null ? dto.getIsOnline() : false);
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());

        return entity;
    }

    public StoreResponseDTO toResponseDTO(Store entity) {
        if (entity == null) {
            return null;
        }

        StoreResponseDTO dto = new StoreResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setIsOnline(entity.getIsOnline());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());

        return dto;
    }

    public void updateEntityFromDTO(StoreRequestDTO dto, Store entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setIsOnline(dto.getIsOnline() != null ? dto.getIsOnline() : false);
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
    }

}