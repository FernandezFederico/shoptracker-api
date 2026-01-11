package com.shoptracker.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    @NotNull(message = "El ID del compra no puede ser nulo")
    @Min(value = 1, message = "El ID del producto debe ser mayor o igual a 1")
    private Long productId;

    @NotNull(message = "El ID de lA compra no puede ser nulo")
    @Min(value = 1, message = "El ID del comercio debe ser mayor o igual a 1")
    private Long storeId;

    @NotNull(message = "La cantidad no puede ser nula")
    @DecimalMin(value = "0.01", message = "La cantidad mínima es 0.01")
    private BigDecimal quantity;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal unitPrice;

    private LocalDate purchaseDate;

}