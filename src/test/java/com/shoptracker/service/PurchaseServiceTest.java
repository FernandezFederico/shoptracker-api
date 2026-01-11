package com.shoptracker.service;

import com.shoptracker.exception.InvalidDataException;
import com.shoptracker.mapper.PurchaseMapper;
import com.shoptracker.model.dto.PurchaseRequestDTO;
import com.shoptracker.model.dto.PurchaseResponseDTO;
import com.shoptracker.model.entity.Product;
import com.shoptracker.model.entity.Purchase;
import com.shoptracker.model.entity.Store;
import com.shoptracker.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PurchaseService Tests")
class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PurchaseMapper purchaseMapper;

    @InjectMocks
    private PurchaseService purchaseService;

    private PurchaseRequestDTO validRequestDTO;
    private Purchase validPurchase;
    private PurchaseResponseDTO validResponseDTO;

    @BeforeEach
    void setUp() {
        // Datos de prueba que usaremos en todos los tests
        validRequestDTO = new PurchaseRequestDTO();
        validRequestDTO.setProductId(1L);
        validRequestDTO.setStoreId(1L);
        validRequestDTO.setQuantity(new BigDecimal("2"));
        validRequestDTO.setUnitPrice(new BigDecimal("1.50"));
        validRequestDTO.setPurchaseDate(LocalDate.of(2026, 1, 5));

        validPurchase = new Purchase();
        validPurchase.setId(1L);
        validPurchase.setProduct(new Product());
        validPurchase.setStore(new Store());
        validPurchase.setQuantity(new BigDecimal("2"));
        validPurchase.setUnitPrice(new BigDecimal("1.50"));
        validPurchase.setTotalPrice(new BigDecimal("3.00"));
        validPurchase.setPurchaseDate(LocalDate.of(2026, 1, 5));

        validResponseDTO = new PurchaseResponseDTO();
        validResponseDTO.setId(1L);
        validResponseDTO.setProductId(1L);
        validResponseDTO.setStoreId(1L);
        validResponseDTO.setQuantity(new BigDecimal("2"));
        validResponseDTO.setUnitPrice(new BigDecimal("1.50"));
        validResponseDTO.setTotalPrice(new BigDecimal("3.00"));
        validResponseDTO.setPurchaseDate(LocalDate.of(2026, 1, 5));
    }

    // ============================================
    // TESTS DEL MÉTODO CREATE
    // ============================================

    @Test
    @DisplayName("create() - Debe crear compra exitosamente")
    void create_shouldCreatePurchaseSuccessfully() {
        // ARRANGE (Preparar)
        when(purchaseMapper.toEntity(validRequestDTO)).thenReturn(validPurchase);
        when(purchaseRepository.save(any(Purchase.class))).thenReturn(validPurchase);
        when(purchaseMapper.toResponseDTO(validPurchase)).thenReturn(validResponseDTO);

        // ACT (Actuar)
        PurchaseResponseDTO result = purchaseService.create(validRequestDTO);

        // ASSERT (Verificar)
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("3.00"), result.getTotalPrice());

        // Verificar que se llamaron los métodos correctos
        verify(purchaseMapper).toEntity(validRequestDTO);
        verify(purchaseRepository).save(any(Purchase.class));
        verify(purchaseMapper).toResponseDTO(validPurchase);
    }

    @Test
    @DisplayName("create() - Debe calcular totalPrice automáticamente")
    void create_shouldCalculateTotalPriceAutomatically() {
        // ARRANGE
        validPurchase.setTotalPrice(null); // Simula que no tiene totalPrice
        when(purchaseMapper.toEntity(validRequestDTO)).thenReturn(validPurchase);
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase saved = invocation.getArgument(0);
            // Verificar que totalPrice fue calculado
            assertNotNull(saved.getTotalPrice());
            assertEquals(new BigDecimal("3.00"), saved.getTotalPrice());
            return saved;
        });
        when(purchaseMapper.toResponseDTO(any())).thenReturn(validResponseDTO);

        // ACT
        purchaseService.create(validRequestDTO);

        // ASSERT
        verify(purchaseRepository).save(any(Purchase.class));
    }

    @Test
    @DisplayName("create() - Debe lanzar excepción cuando fecha es futura")
    void create_shouldThrowException_whenDateIsFuture() {
        // ARRANGE
        validRequestDTO.setPurchaseDate(LocalDate.now().plusDays(1));

        // ACT & ASSERT
        assertThrows(InvalidDataException.class,
                () -> purchaseService.create(validRequestDTO));

        // Verificar que NO se guardó nada
        verify(purchaseRepository, never()).save(any());
    }

    @Test
    @DisplayName("create() - Debe usar fecha actual cuando no viene fecha")
    void create_shouldUseCurrentDate_whenDateIsNull() {
        // ARRANGE
        validRequestDTO.setPurchaseDate(null);
        validPurchase.setPurchaseDate(null);

        when(purchaseMapper.toEntity(validRequestDTO)).thenReturn(validPurchase);
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase saved = invocation.getArgument(0);
            // Verificar que se seteo fecha actual
            assertEquals(LocalDate.now(), saved.getPurchaseDate());
            return saved;
        });
        when(purchaseMapper.toResponseDTO(any())).thenReturn(validResponseDTO);

        // ACT
        purchaseService.create(validRequestDTO);

        // ASSERT
        verify(purchaseRepository).save(any(Purchase.class));
    }
}