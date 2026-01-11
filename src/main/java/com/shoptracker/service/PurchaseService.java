package com.shoptracker.service;

import com.shoptracker.exception.InvalidDataException;
import com.shoptracker.exception.ResourceNotFoundException;
import com.shoptracker.mapper.PurchaseMapper;
import com.shoptracker.model.dto.PurchaseListResponseDTO;
import com.shoptracker.model.dto.PurchaseRequestDTO;
import com.shoptracker.model.dto.PurchaseResponseDTO;
import com.shoptracker.model.dto.PurchaseSummaryResponseDTO;
import com.shoptracker.model.entity.Purchase;
import com.shoptracker.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;

    public PurchaseResponseDTO create(PurchaseRequestDTO requestDTO) {
        // Validar fecha no futura
        if (requestDTO.getPurchaseDate() != null && requestDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new InvalidDataException("La fecha de compra no puede ser futura");
        }

        Purchase entity = purchaseMapper.toEntity(requestDTO);
        entity.setTotalPrice(entity.getQuantity().multiply(entity.getUnitPrice()));
        if (entity.getPurchaseDate() == null) {
            entity.setPurchaseDate(LocalDate.now());
        }
        Purchase savedEntity = purchaseRepository.save(entity);

        return purchaseMapper.toResponseDTO(savedEntity);
    }

    public PurchaseListResponseDTO findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Purchase> purchasePage = purchaseRepository.findAll(pageable);

        List<PurchaseResponseDTO> purchaseDTOs = purchasePage.getContent()
                .stream()
                .map(purchaseMapper::toResponseDTO)
                .collect(Collectors.toList());

        return new PurchaseListResponseDTO(
                purchaseDTOs,
                purchasePage.getTotalElements(),
                page,
                pageSize
        );
    }

    public PurchaseResponseDTO findById(Long id) {
        Purchase entity = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compra no encontrada con id: " + id));

        return purchaseMapper.toResponseDTO(entity);
    }

    public PurchaseResponseDTO update(Long id, PurchaseRequestDTO requestDTO) {  // ← Orden corregido
        Purchase existing = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compra no encontrada con id: " + id));


        if (requestDTO.getPurchaseDate() != null && requestDTO.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new InvalidDataException("La fecha de compra no puede ser futura");
        }

        purchaseMapper.updateEntityFromDTO(requestDTO, existing);
        existing.setTotalPrice(existing.getQuantity().multiply(existing.getUnitPrice()));
        Purchase updatedEntity = purchaseRepository.save(existing);
        return purchaseMapper.toResponseDTO(updatedEntity);
    }

    public void deleteById(Long id) {
        if (!purchaseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Compra no encontrada con id: " + id);
        }
        purchaseRepository.deleteById(id);
    }

    public PurchaseSummaryResponseDTO getSummary(LocalDate dateFrom, LocalDate dateTo) {
        // Si no vienen fechas, usar todo el historial
        if (dateFrom == null) {
            dateFrom = LocalDate.of(2000, 1, 1);  // Fecha muy antigua
        }
        if (dateTo == null) {
            dateTo = LocalDate.now();
        }

        // Validar que dateFrom no sea posterior a dateTo
        if (dateFrom.isAfter(dateTo)) {
            throw new InvalidDataException("La fecha inicial no puede ser posterior a la fecha final");
        }

        // Obtener compras del período
        List<Purchase> purchases = purchaseRepository.findByPurchaseDateBetween(dateFrom, dateTo);

        // Calcular estadísticas
        BigDecimal totalSpent = purchases.stream()
                .map(Purchase::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalPurchases = purchases.size();

        BigDecimal averagePerPurchase = totalPurchases > 0
                ? totalSpent.divide(BigDecimal.valueOf(totalPurchases), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // Construir respuesta
        PurchaseSummaryResponseDTO.PeriodDTO period = new PurchaseSummaryResponseDTO.PeriodDTO(
                dateFrom.toString(),
                dateTo.toString()
        );

        return new PurchaseSummaryResponseDTO(
                totalSpent,
                totalPurchases,
                averagePerPurchase,
                period
        );
    }
}