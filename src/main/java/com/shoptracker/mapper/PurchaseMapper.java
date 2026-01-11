package com.shoptracker.mapper;

import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.model.dto.PurchaseRequestDTO;
import com.shoptracker.model.dto.PurchaseResponseDTO;
import com.shoptracker.model.entity.Product;
import com.shoptracker.model.entity.Purchase;
import com.shoptracker.model.entity.Store;
import com.shoptracker.repository.ProductRepository;
import com.shoptracker.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseMapper {

    private final ProductRepository productRepository;
    private  final StoreRepository storeRepository;

    public Purchase toEntity(PurchaseRequestDTO dto){
        if (dto == null) {
            return null;
        }

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(()-> new ResourceNotFoundException(
                        "No se encuentra el comercio con id: " + dto.getStoreId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra el producto con id: " + dto.getProductId()));

        Purchase entity = new Purchase();
        entity.setStore(store);
        entity.setProduct(product);
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setPurchaseDate(dto.getPurchaseDate());

        return entity;
    }

    public PurchaseResponseDTO toResponseDTO(Purchase entity) {
        if (entity == null) {
            return null;
        }

        PurchaseResponseDTO dto = new PurchaseResponseDTO();
        dto.setId(entity.getId());
        dto.setProductId(entity.getProduct().getId());
        dto.setStoreId(entity.getStore().getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setPurchaseDate(entity.getPurchaseDate());

        return dto;
    }

    public void updateEntityFromDTO(PurchaseRequestDTO dto, Purchase entity) {
        if (dto == null || entity == null) {
            return;
        }

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra el comercio con id: " + dto.getStoreId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encuentra el producto con id: " + dto.getProductId()));

        entity.setStore(store);
        entity.setProduct(product);
        entity.setQuantity(dto.getQuantity());
        entity.setUnitPrice(dto.getUnitPrice());
        entity.setPurchaseDate(dto.getPurchaseDate());
    }
}
