package com.shoptracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponseDTO {

    private List<ProductResponseDTO> items;
    private long total;
    private int page;
    private int pageSize;
}
