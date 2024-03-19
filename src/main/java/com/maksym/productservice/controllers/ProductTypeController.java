package com.maksym.productservice.controllers;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.service.ProductTypeServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-type")
public class ProductTypeController {

    private ProductTypeServiceImpl productTypeService;

    public ProductTypeController(ProductTypeServiceImpl productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ProductTypeRequest request){
        return new ResponseEntity<>(productTypeService.add(request), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@PathVariable("id") Long id, @RequestBody ProductTypeRequest request){
        return new ResponseEntity<>(productTypeService.update(id,request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(){
        return new ResponseEntity<>(productTypeService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id){
        return new ResponseEntity<>(productTypeService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        productTypeService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
