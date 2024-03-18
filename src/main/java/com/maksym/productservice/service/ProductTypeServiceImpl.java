package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.dtoMapper.ProductTypeRequestMapper;
import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public ProductType add(ProductTypeRequest request) {
        ProductType productType = ProductTypeRequestMapper.toModel(request);
        return productTypeRepository.save(productType);
    }

    @Override
    public Boolean deleteById(Long id) {
        productTypeRepository.deleteById(id);
        return true;
    }

    @Override
    public ProductType update(Long id, ProductTypeRequest request) {
        ProductType updatedProductType = productTypeRepository.findById(id).orElseThrow(()->new EntityNotFoundException("ProductType with id: "+id+" is not found"));
        updatedProductType.setId(id);
        if(request.getName()!=null && !request.getName().isEmpty())updatedProductType.setName(request.getName());
        return productTypeRepository.save(updatedProductType);
    }

    @Override
    public List<ProductType> getAll() {
        return productTypeRepository.findAll();
    }
}
