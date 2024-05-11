package com.maksym.productservice.staticObject;

import com.maksym.productservice.dto.request.ProductDtoRequest;
import com.maksym.productservice.dto.response.ProductDtoResponse;
import com.maksym.productservice.model.Product;

import java.math.BigDecimal;

public class StaticProduct {

    public static final Long ID = 1L;

    public static Product product1() {
        Product model = new Product();
        model.setId(ID);
        model.setName("name");
        model.setType(StaticProductType.productType1());
        model.setDescription("description");
        model.setPrice(new BigDecimal(10));
        model.setRating(1);
        return model;
    }

    public static Product product2() {
        Product model = new Product();
        model.setId(ID);
        model.setName("name");
        model.setType(StaticProductType.productType2());
        model.setDescription("description");
        model.setPrice(new BigDecimal(20));
        model.setRating(2);
        return model;
    }

    public static ProductDtoRequest productDtoRequest1() {
        ProductDtoRequest dtoRequest = new ProductDtoRequest();
        dtoRequest.setName("name");
        dtoRequest.setTypeId(1L);
        dtoRequest.setDescription("description");
        dtoRequest.setPrice(new BigDecimal(10));
        dtoRequest.setRating(1);
        return dtoRequest;
    }

    public static ProductDtoResponse productDtoResponse1() {
        ProductDtoResponse dtoResponse = new ProductDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setName("name");
        dtoResponse.setType(StaticProductType.productTypeDtoResponse1());
        dtoResponse.setDescription("description");
        dtoResponse.setPrice(new BigDecimal(10));
        dtoResponse.setRating(1);
        return dtoResponse;
    }

    public static ProductDtoResponse productDtoResponse2() {
        ProductDtoResponse dtoResponse = new ProductDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setName("name");
        dtoResponse.setType(StaticProductType.productTypeDtoResponse1());
        dtoResponse.setDescription("description");
        dtoResponse.setPrice(new BigDecimal(20));
        dtoResponse.setRating(2);
        return dtoResponse;
    }
}
