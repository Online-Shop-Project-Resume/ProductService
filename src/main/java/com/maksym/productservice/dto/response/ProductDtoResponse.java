package com.maksym.productservice.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDtoResponse {

    private Long id;

    private String name;

    private ProductTypeDtoResponse type;

    private String description;

    private BigDecimal price;

    private Integer rating;
}
