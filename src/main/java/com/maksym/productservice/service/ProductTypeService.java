package com.maksym.productservice.service;

import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public ProductType create(ProductType productType) {
        log.info("ProductType create: {}", productType);

        return productTypeRepository.save(productType);
    }

    public ProductType getById(Long id) {
        log.info("ProductType get by id: {}", id);
        return productTypeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("ProductType with id: " + id + " does not exist"));
    }

    public Page<ProductType> getAll(Pageable pageable) {
        log.info("ProductType get all: {}", pageable);
        return productTypeRepository.findAll(pageable);
    }

    public ProductType updateById(Long id, ProductType productType) {
        getById(id);
        productType.setId(id);

        log.info("ProductType update by id: {}", productType);
        return productTypeRepository.save(productType);
    }

    public Boolean deleteById(Long id) {
        log.info("ProductType delete by id: {}", id);
        productTypeRepository.deleteById(id);
        return true;
    }
}
