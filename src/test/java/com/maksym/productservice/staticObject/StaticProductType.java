package com.maksym.productservice.staticObject;

import com.maksym.productservice.dto.request.ProductTypeDtoRequest;
import com.maksym.productservice.dto.response.ProductTypeDtoResponse;
import com.maksym.productservice.model.ProductType;


public class StaticProductType {

    public static final Long ID = 1L;

    public static ProductType productType1() {
        ProductType model = new ProductType();
        model.setId(ID);
        model.setName("name");
        return model;
    }

    public static ProductType productType2() {
        ProductType model = new ProductType();
        model.setId(ID);
        model.setName("name");
        return model;
    }

    public static ProductTypeDtoRequest productTypeDtoRequest1() {
        ProductTypeDtoRequest dtoRequest = new ProductTypeDtoRequest();
        dtoRequest.setName("name");
        return dtoRequest;
    }

    public static ProductTypeDtoResponse productTypeDtoResponse1() {
        ProductTypeDtoResponse dtoResponse = new ProductTypeDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setName("name");
        return dtoResponse;
    }

    public static ProductTypeDtoResponse productTypeDtoResponse2() {
        ProductTypeDtoResponse dtoResponse = new ProductTypeDtoResponse();
        dtoResponse.setId(ID);
        dtoResponse.setName("name");
        return dtoResponse;
    }
}
