package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.model.ProductType;

import java.util.List;

public interface ProductTypeService {
    public ProductType add(ProductTypeRequest request);
    public Boolean deleteById(Long id);
    public ProductType update(Long id, ProductTypeRequest request);
    public List<ProductType> getAll();
    public ProductType getById(Long id);

}
