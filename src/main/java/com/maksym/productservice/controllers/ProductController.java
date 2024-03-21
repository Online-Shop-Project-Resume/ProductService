package com.maksym.productservice.controllers;

import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.service.ProductService;
import com.maksym.productservice.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.add(productRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable("id") Long id){
        return new ResponseEntity<>(productService.get(id), HttpStatus.OK);
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Object> existsProduct(@PathVariable("id") Long id){
        return new ResponseEntity<>(productService.exist(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllProducts(){
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequest productRequest){
        return new ResponseEntity<>(productService.update(id, productRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id){
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
