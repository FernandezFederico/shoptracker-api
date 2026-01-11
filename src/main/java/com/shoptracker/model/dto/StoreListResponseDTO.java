package com.shoptracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreListResponseDTO {

    private List<StoreResponseDTO> items;
    private long total;
    private int page;
    private int pageSize;
}