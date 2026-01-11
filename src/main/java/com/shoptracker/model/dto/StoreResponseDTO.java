package com.shoptracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private Boolean isOnline;
    private BigDecimal latitude;
    private BigDecimal longitude;
}