package com.maksym.productservice.service;


import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.repository.ProductRepository;
import com.maksym.productservice.staticObject.StaticProduct;
import com.maksym.productservice.staticObject.StaticProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductTypeService productTypeService;
    @InjectMocks
    private ProductService productService;
    private final Product product = StaticProduct.product1();
    private final Product product2 = StaticProduct.product2();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        when(productTypeService.getById(StaticProductType.ID)).thenReturn(StaticProductType.productType1());
	    when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertNotNull(createdProduct);
        assertEquals(product, createdProduct);
        verify(productTypeService, times(1)).getById(StaticProductType.ID);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testCreate_EntityNotFoundException_ProductTypeNotFound() {
        when(productTypeService.getById(StaticProductType.ID)).thenThrow(new EntityNotFoundException("ProductType not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.create(product));

        assertNotNull(exception);
        assertEquals("ProductType not found", exception.getMessage());
        verify(productTypeService, times(1)).getById(StaticProductType.ID);
        verifyNoInteractions(productRepository);
    }

    @Test
    void testCreate_DataAccessException() {
        when(productTypeService.getById(StaticProductType.ID)).thenReturn(StaticProductType.productType1());
        when(productRepository.findById(StaticProduct.ID)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getById(StaticProduct.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productRepository, times(1)).findById(StaticProduct.ID);
    }

    @Test
    void testGetAll() {
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);
        Page<Product> productPage = new PageImpl<>(productList);
        Pageable pageable = Pageable.unpaged();
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<Product> result = productService.getAll(pageable);

        assertEquals(productList.size(), result.getSize());
        assertEquals(product, result.getContent().get(0));
        assertEquals(product2, result.getContent().get(1));
    }

    @Test
    void testGetAll_AnyException() {
        when(productRepository.findAll(any(Pageable.class))).thenThrow(new DataAccessException("Database connection failed") {});

        Pageable pageable = Pageable.unpaged();
        RuntimeException exception = assertThrows(DataAccessException.class, () -> productService.getAll(pageable));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate_Success() {
	    Product existingProduct = StaticProduct.product1();
        Product updatedProduct = StaticProduct.product2();
        when(productTypeService.getById(StaticProductType.ID)).thenReturn(StaticProductType.productType1());
	    when(productRepository.findById(StaticProduct.ID)).thenReturn(java.util.Optional.of(existingProduct));
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateById(StaticProduct.ID, updatedProduct);

        assertEquals(updatedProduct, result);
        verify(productRepository, times(1)).findById(StaticProduct.ID);
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateById_EntityNotFoundException_ProductTypeNotFound() {
        when(productRepository.findById(StaticProduct.ID)).thenReturn(java.util.Optional.of(product));
        when(productTypeService.getById(StaticProductType.ID)).thenThrow(new EntityNotFoundException("ProductType not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> productService.updateById(StaticProduct.ID, product));

        assertNotNull(exception);
        assertEquals("ProductType not found", exception.getMessage());
        verify(productTypeService, times(1)).getById(StaticProductType.ID);
    }


    @Test
    void testUpdateById_EntityNotFoundException() {
        Product updatedProduct = StaticProduct.product1();
        when(productRepository.findById(StaticProduct.ID)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateById(StaticProduct.ID, updatedProduct));
        verify(productRepository, times(1)).findById(StaticProduct.ID);
        verify(productRepository, never()).save(updatedProduct);
    }

    @Test
    void testUpdateById_AnyException() {
        Product existingProduct = StaticProduct.product1();
        Product updatedProduct = StaticProduct.product2();
        when(productRepository.findById(StaticProduct.ID)).thenReturn(java.util.Optional.of(existingProduct));
        when(productTypeService.getById(StaticProductType.ID)).thenReturn(StaticProductType.productType1());
	    when(productRepository.save(updatedProduct)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.updateById(StaticProduct.ID, updatedProduct));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testDeleteById_Success() {
        boolean result = productService.deleteById(StaticProduct.ID);

        verify(productRepository).deleteById(StaticProduct.ID);
        assertTrue(result);
    }

    @Test
    void testDeleteById_AnyException() {
        doThrow(new DataAccessException("Database connection failed") {}).when(productRepository).deleteById(StaticProduct.ID);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteById(StaticProduct.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productRepository, times(1)).deleteById(StaticProduct.ID);
    }
}