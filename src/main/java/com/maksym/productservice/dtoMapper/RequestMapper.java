package com.maksym.productservice.dtoMapper;

import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.model.Product;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public static Product toProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setType(productRequest.getType());
        product.setPrice(productRequest.getPrice());
        return product;
    }

    public static ProductRequest toProductRequest(Product product) {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(product.getName());
        productRequest.setDescription(product.getDescription());
        productRequest.setType(product.getType());
        productRequest.setPrice(product.getPrice());
        return productRequest;
    }
}
