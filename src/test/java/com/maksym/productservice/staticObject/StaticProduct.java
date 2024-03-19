package com.maksym.productservice.staticObject;

import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.model.ProductType;

import java.math.BigDecimal;

public class StaticProduct {
    public static Product product1(){
        ProductType productType = new ProductType(1L, "type");
        return new Product(1L, "Product 1", productType,"Description 1",  BigDecimal.TEN);
    }
    public static Product product2(){
        ProductType productType = new ProductType(1L, "type");
        return new Product(2L, "Product 2", productType, "Description 2", BigDecimal.valueOf(20));
    }
    public static ProductRequest productRequest1(){
        ProductType productType = new ProductType(1L,"type");
        return new ProductRequest("Product 1", "Description 1", productType, BigDecimal.ONE);
    }
}
