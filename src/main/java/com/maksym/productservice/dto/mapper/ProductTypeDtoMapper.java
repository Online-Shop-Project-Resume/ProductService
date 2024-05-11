package com.maksym.productservice.dto.mapper;

import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.dto.request.ProductTypeDtoRequest;
import com.maksym.productservice.dto.response.ProductTypeDtoResponse;

public class ProductTypeDtoMapper {

    public static ProductType toModel(ProductTypeDtoRequest request) {
        ProductType model = new ProductType();

        model.setName(request.getName());

        return model;
    }

    public static ProductTypeDtoResponse toResponse(ProductType model) {
        ProductTypeDtoResponse response = new ProductTypeDtoResponse();

        response.setId(model.getId());
        response.setName(model.getName());

        return response;
    }

    private ProductTypeDtoMapper() {}

}
