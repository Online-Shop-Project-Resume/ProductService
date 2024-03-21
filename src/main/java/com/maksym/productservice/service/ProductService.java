package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.model.Product;

import java.util.List;

public interface ProductService{
    public Product add(ProductRequest productRequest);
    public void delete(Long id);
    public Product update(Long id, ProductRequest productRequest);
    public Product get(Long id);
    public List<Product> getAll();

    Boolean exist(Long id);
}
