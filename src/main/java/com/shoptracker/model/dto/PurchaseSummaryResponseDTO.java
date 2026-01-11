package com.shoptracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSummaryResponseDTO {

    private BigDecimal totalSpent;           // Total gastado en el período
    private Integer totalPurchases;          // Número de compras
    private BigDecimal averagePerPurchase;   // Promedio por compra
    private PeriodDTO period;                // Período consultado

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    //si luego se necesita en otros lugares se cresa separado
    public static class PeriodDTO {
        private String from;  // Fecha inicio (YYYY-MM-DD)
        private String to;    // Fecha fin (YYYY-MM-DD)
    }
}