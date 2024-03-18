package com.maksym.productservice.dtoMapper;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.model.ProductType;
import org.springframework.stereotype.Component;

@Component
public class ProductTypeRequestMapper {
    public static ProductType toModel(ProductTypeRequest request){
        ProductType productType = new ProductType();
        productType.setName(request.getName());
        return productType;
    }
}
