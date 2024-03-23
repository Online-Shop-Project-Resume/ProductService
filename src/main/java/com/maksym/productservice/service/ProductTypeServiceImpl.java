package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.dtoMapper.ProductTypeRequestMapper;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductType add(ProductTypeRequest request) {
        log.info("Product add: {}", request);
        ProductType productType = ProductTypeRequestMapper.toModel(request);
        return productTypeRepository.save(productType);
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Product delete by id: {}", id);
        productTypeRepository.deleteById(id);
        return true;
    }

    @Override
    public ProductType update(Long id, ProductTypeRequest request) {
        log.info("Product update by id: {}, ProductTypeRequest: {}", id, request);
        ProductType updatedProductType = productTypeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("ProductType with id: "+id+" is not found"));
        updatedProductType.setId(id);
        if(request.getName()!=null && !request.getName().isEmpty())updatedProductType.setName(request.getName());
        return productTypeRepository.save(updatedProductType);
    }

    @Override
    public List<ProductType> getAll() {
        log.info("Product get all");
        return productTypeRepository.findAll();
    }

    @Override
    public ProductType getById(Long id) {
        log.info("Product get by id: {}", id);
        return productTypeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("ProductType with id: "+id+" is not found"));
    }
}
