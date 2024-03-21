package com.maksym.productservice.service;

import com.maksym.productservice.dtoMapper.RequestMapper;
import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.repository.ProductRepository;
import com.maksym.productservice.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product add(ProductRequest productRequest) {
        log.info("Product add: {}",productRequest);

        Product product = RequestMapper.toProduct(productRequest);
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        log.info("Product delete by id: {}",id);
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Long id, ProductRequest productRequest) {
        log.info("Product update by id: {}, product: {}", id, productRequest);
        Product updatedProduct = RequestMapper.toProduct(productRequest);
        updatedProduct.setId(id);
        if(!productRepository.existsById(id)) throw new EntityNotFoundException("User with id: "+id+ " doesn't exist.");
        return productRepository.save(updatedProduct);
    }

    @Override
    public Product get(Long id) {
        log.info("Product get by id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " doesn't exist."));
    }

    @Override
    public List<Product> getAll() {
        log.info("Product get all");
        return productRepository.findAll();
    }

    @Override
    public Boolean exist(Long id) {
        log.info("Product exists by id: {}", id);
        return productRepository.existsById(id);
    }
}
