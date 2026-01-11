package com.shoptracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseListResponseDTO {

    private List<PurchaseResponseDTO> items;
    private long total;
    private int page;
    private int pageSize;
}
