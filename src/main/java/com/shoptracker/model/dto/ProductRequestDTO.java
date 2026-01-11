package com.shoptracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "La nombre es obligatoria")
    @Size(min = 1, max = 100, message = "El producto debe tener entre 1 y 100 caracteres")
    private String name;

    @NotNull(message = "El ID de categoría debe ser mayor a 0")
    private Long categoryId;

    @NotNull(message = "La unidad es obligatoria")
    private Long unitId;
}
