package com.maksym.productservice.controller;

import com.maksym.productservice.dto.mapper.ProductTypeDtoMapper;
import com.maksym.productservice.dto.request.ProductTypeDtoRequest;
import com.maksym.productservice.dto.response.ProductTypeDtoResponse;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.service.ProductTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-type")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping
    @Operation(summary = "Create an productType", description = "Create new productType")
    @ApiResponse(responseCode = "201", description = "ProductType saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Invalid foreign key that is not found")
    @ApiResponse(responseCode = "503", description = "Database connection failed")
    public ResponseEntity<ProductTypeDtoResponse> createProductType(@Valid @RequestBody ProductTypeDtoRequest productTypeDtoRequest) {
        ProductType productType = ProductTypeDtoMapper.toModel(productTypeDtoRequest);
        productType = productTypeService.create(productType);
        return new ResponseEntity<>(ProductTypeDtoMapper.toResponse(productType), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ProductType", description = "Get ProductType By Id")
    @ApiResponse(responseCode = "200", description = "ProductType Get successfully")
    @ApiResponse(responseCode = "404", description = "ProductType with such an Id not found")
    public ResponseEntity<ProductTypeDtoResponse> getProductTypeById(@PathVariable("id") Long id) {
        ProductType productType = productTypeService.getById(id);
        return new ResponseEntity<>(ProductTypeDtoMapper.toResponse(productType), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get All ProductType", description = "Get All ProductType")
    @ApiResponse(responseCode = "200", description = "ProductType Get All successfully")
    @ApiResponse(responseCode = "404", description = "No records with ProductType have been found")
    public ResponseEntity<Page<ProductTypeDtoResponse>> getAllProductType(Pageable pageable) {
        Page<ProductType> productTypePage = productTypeService.getAll(pageable);
        return new ResponseEntity<>(productTypePage.map(ProductTypeDtoMapper::toResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an productType", description = "Update an productType by Id and new ProductType")
    @ApiResponse(responseCode = "201", description = "ProductType updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "ProductType with such an Id not found or invalid foreign key that is not found")
    public ResponseEntity<ProductTypeDtoResponse> updateProductType(@PathVariable("id") Long id, @Valid @RequestBody ProductTypeDtoRequest productTypeDtoRequest) {
        ProductType productType = ProductTypeDtoMapper.toModel(productTypeDtoRequest);
        productType = productTypeService.updateById(id, productType);
        return new ResponseEntity<>(ProductTypeDtoMapper.toResponse(productType), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an productType", description = "Delete an productType by id")
    @ApiResponse(responseCode = "204", description = "ProductType deleted successfully")
    public ResponseEntity<Boolean> deleteProductType(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productTypeService.deleteById(id), HttpStatus.NO_CONTENT);
    }
}