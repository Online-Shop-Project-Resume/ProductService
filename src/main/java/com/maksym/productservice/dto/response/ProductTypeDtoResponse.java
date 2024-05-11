package com.maksym.productservice.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductTypeDtoResponse {

    private Long id;

    private String name;
}
