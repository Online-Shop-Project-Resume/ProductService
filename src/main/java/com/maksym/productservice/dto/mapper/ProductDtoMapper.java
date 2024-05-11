package com.maksym.productservice.dto.mapper;

import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.dto.request.ProductDtoRequest;
import com.maksym.productservice.dto.response.ProductDtoResponse;

public class ProductDtoMapper {

    public static Product toModel(ProductDtoRequest request) {
        Product model = new Product();

        model.setName(request.getName());
        ProductType type = new ProductType();
        type.setId(request.getTypeId());
        model.setType(type);
        model.setDescription(request.getDescription());
        model.setPrice(request.getPrice());
        model.setRating(request.getRating());

        return model;
    }

    public static ProductDtoResponse toResponse(Product model) {
        ProductDtoResponse response = new ProductDtoResponse();

        response.setId(model.getId());
        response.setName(model.getName());
        response.setType(ProductTypeDtoMapper.toResponse(model.getType()));
        response.setDescription(model.getDescription());
        response.setPrice(model.getPrice());
        response.setRating(model.getRating());

        return response;
    }

    private ProductDtoMapper() {}

}
