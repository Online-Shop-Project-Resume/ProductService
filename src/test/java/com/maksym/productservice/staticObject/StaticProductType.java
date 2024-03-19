package com.maksym.productservice.staticObject;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.model.ProductType;

public class StaticProductType {
    public static ProductType productType1(){
        return new ProductType(1L, "name");
    }

    public static ProductTypeRequest productTypeRequest1(){
        return new ProductTypeRequest("name");
    }
    public static ProductTypeRequest productTypeRequest2(){
        return new ProductTypeRequest("name2");
    }

    public static ProductTypeRequest productTypeRequestInvalid() {
        return new ProductTypeRequest("");
    }
}
