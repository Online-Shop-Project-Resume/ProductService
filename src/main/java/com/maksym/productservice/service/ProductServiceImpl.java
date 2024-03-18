package com.maksym.productservice.service;

import com.maksym.productservice.dtoMapper.RequestMapper;
import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductRepository;
import com.maksym.productservice.repository.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public Product add(ProductRequest productRequest) {
        Product product = RequestMapper.toProduct(productRequest);
        log.info("Product add: {}",productRequest);
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        log.info("Product delete by id: {}",id);
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Long id, ProductRequest productRequest) {
        Product product = RequestMapper.toProduct(productRequest);
        product.setId(id);
        Product updatedProduct = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User with id: "+id+ " doesn't exist."));
        if (product.getName() != null && !product.getName().isBlank()) {
            updatedProduct.setName(product.getName());
        }
        if (product.getDescription() != null && !product.getDescription().isBlank()) {
            updatedProduct.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            updatedProduct.setPrice(product.getPrice());
        }
        log.info("Product update by id: {}, product: {}", id, productRequest);
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
}
