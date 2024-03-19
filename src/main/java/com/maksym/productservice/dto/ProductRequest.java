package com.maksym.productservice.dto;

import com.maksym.productservice.model.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    @NotBlank(message = "Description can't be blank")
    private String description;
    private ProductType type;
    @Positive(message = "Price has to be a positive number")
    private BigDecimal price;
}
