package com.maksym.productservice.controller;

import com.maksym.productservice.dto.mapper.ProductDtoMapper;
import com.maksym.productservice.dto.request.ProductDtoRequest;
import com.maksym.productservice.dto.response.ProductDtoResponse;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create an product", description = "Create new product")
    @ApiResponse(responseCode = "201", description = "Product saved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Invalid foreign key that is not found")
    @ApiResponse(responseCode = "503", description = "Database connection failed")
    public ResponseEntity<ProductDtoResponse> createProduct(@Valid @RequestBody ProductDtoRequest productDtoRequest) {
        Product product = ProductDtoMapper.toModel(productDtoRequest);
        product = productService.create(product);
        return new ResponseEntity<>(ProductDtoMapper.toResponse(product), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product", description = "Get Product By Id")
    @ApiResponse(responseCode = "200", description = "Product Get successfully")
    @ApiResponse(responseCode = "404", description = "Product with such an Id not found")
    public ResponseEntity<ProductDtoResponse> getProductById(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        return new ResponseEntity<>(ProductDtoMapper.toResponse(product), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Get All Product", description = "Get All Product")
    @ApiResponse(responseCode = "200", description = "Product Get All successfully")
    @ApiResponse(responseCode = "404", description = "No records with Product have been found")
    public ResponseEntity<Page<ProductDtoResponse>> getAllProduct(Pageable pageable) {
        Page<Product> productPage = productService.getAll(pageable);
        return new ResponseEntity<>(productPage.map(ProductDtoMapper::toResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an product", description = "Update an product by Id and new Product")
    @ApiResponse(responseCode = "201", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Product with such an Id not found or invalid foreign key that is not found")
    public ResponseEntity<ProductDtoResponse> updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDtoRequest productDtoRequest) {
        Product product = ProductDtoMapper.toModel(productDtoRequest);
        product = productService.updateById(id, product);
        return new ResponseEntity<>(ProductDtoMapper.toResponse(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an product", description = "Delete an product by id")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.deleteById(id), HttpStatus.NO_CONTENT);
    }
}