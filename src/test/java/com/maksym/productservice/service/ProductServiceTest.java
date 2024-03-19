package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductRequest;
import com.maksym.productservice.dtoMapper.RequestMapper;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.repository.ProductRepository;
import com.maksym.productservice.staticObject.StaticProduct;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void testAddProduct() {
        ProductRequest request = StaticProduct.productRequest1();
        Product product = RequestMapper.toProduct(request);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product response = productService.add(request);
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getPrice(), response.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;
        Mockito.doNothing().when(productRepository).deleteById(id);
        productService.delete(id);
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    public void testUpdateProduct() {
        Long id = 1L;
        ProductRequest request = StaticProduct.productRequest1();
        Product product = StaticProduct.product1();
        Mockito.when(productRepository.existsById(id)).thenReturn(true);
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(product);
        Product response = productService.update(id, request);
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    public void testGetProduct() {
        Long id = 1L;
        Product product = StaticProduct.product1();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Product response = productService.get(id);
        assertEquals(product.getName(), response.getName());
        assertEquals(product.getDescription(), response.getDescription());
        assertEquals(product.getPrice(), response.getPrice());
    }

    @Test
    public void testGetAllProducts() {
        List<Product> productList = List.of(
                StaticProduct.product1(),
                StaticProduct.product2()
        );
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        List<Product> responseList = productService.getAll();
        assertEquals(productList.size(), responseList.size());
    }

    @Test
    public void testGetProduct_NotFound() {
        Long id = 1L;
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.get(id));
    }
}

