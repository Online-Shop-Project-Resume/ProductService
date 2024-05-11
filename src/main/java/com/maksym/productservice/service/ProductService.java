package com.maksym.productservice.service;

import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductTypeService productTypeService;

    public ProductService(ProductTypeService productTypeService, ProductRepository productRepository) {
        this.productTypeService = productTypeService;
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        log.info("Product create: {}", product);
        product.setType(productTypeService.getById(product.getType().getId()));
        return productRepository.save(product);
    }

    public Product getById(Long id) {
        log.info("Product get by id: {}", id);
        return productRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Product with id: " + id + " does not exist"));
    }

    public Page<Product> getAll(Pageable pageable) {
        log.info("Product get all: {}", pageable);
        return productRepository.findAll(pageable);
    }

    public Product updateById(Long id, Product product) {
        getById(id);
        product.setId(id);
        product.setType(productTypeService.getById(product.getType().getId()));
        log.info("Product update by id: {}", product);
        return productRepository.save(product);
    }

    public Boolean deleteById(Long id) {
        log.info("Product delete by id: {}", id);
        productRepository.deleteById(id);
        return true;
    }
}
