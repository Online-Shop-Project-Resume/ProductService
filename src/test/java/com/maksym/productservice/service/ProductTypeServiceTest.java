package com.maksym.productservice.service;


import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductTypeRepository;
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

class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;
    @InjectMocks
    private ProductTypeService productTypeService;
    private final ProductType productType = StaticProductType.productType1();
    private final ProductType productType2 = StaticProductType.productType2();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
	    when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        ProductType createdProductType = productTypeService.create(productType);

        assertNotNull(createdProductType);
        assertEquals(productType, createdProductType);
        verify(productTypeRepository, times(1)).save(productType);
    }

    @Test
    void testCreate_DataAccessException() {
        when(productTypeRepository.findById(StaticProductType.ID)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productTypeService.getById(StaticProductType.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productTypeRepository, times(1)).findById(StaticProductType.ID);
    }

    @Test
    void testGetAll() {
        List<ProductType> productTypeList = new ArrayList<>();
        productTypeList.add(productType);
        productTypeList.add(productType2);
        Page<ProductType> productTypePage = new PageImpl<>(productTypeList);
        Pageable pageable = Pageable.unpaged();
        when(productTypeRepository.findAll(pageable)).thenReturn(productTypePage);

        Page<ProductType> result = productTypeService.getAll(pageable);

        assertEquals(productTypeList.size(), result.getSize());
        assertEquals(productType, result.getContent().get(0));
        assertEquals(productType2, result.getContent().get(1));
    }

    @Test
    void testGetAll_AnyException() {
        when(productTypeRepository.findAll(any(Pageable.class))).thenThrow(new DataAccessException("Database connection failed") {});

        Pageable pageable = Pageable.unpaged();
        RuntimeException exception = assertThrows(DataAccessException.class, () -> productTypeService.getAll(pageable));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productTypeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testUpdate_Success() {
	    ProductType existingProductType = StaticProductType.productType1();
        ProductType updatedProductType = StaticProductType.productType2();
	    when(productTypeRepository.findById(StaticProductType.ID)).thenReturn(java.util.Optional.of(existingProductType));
        when(productTypeRepository.save(updatedProductType)).thenReturn(updatedProductType);

        ProductType result = productTypeService.updateById(StaticProductType.ID, updatedProductType);

        assertEquals(updatedProductType, result);
        verify(productTypeRepository, times(1)).findById(StaticProductType.ID);
        verify(productTypeRepository, times(1)).save(updatedProductType);
    }


    @Test
    void testUpdateById_EntityNotFoundException() {
        ProductType updatedProductType = StaticProductType.productType1();
        when(productTypeRepository.findById(StaticProductType.ID)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productTypeService.updateById(StaticProductType.ID, updatedProductType));
        verify(productTypeRepository, times(1)).findById(StaticProductType.ID);
        verify(productTypeRepository, never()).save(updatedProductType);
    }

    @Test
    void testUpdateById_AnyException() {
        ProductType existingProductType = StaticProductType.productType1();
        ProductType updatedProductType = StaticProductType.productType2();
        when(productTypeRepository.findById(StaticProductType.ID)).thenReturn(java.util.Optional.of(existingProductType));
	    when(productTypeRepository.save(updatedProductType)).thenThrow(new DataAccessException("Database connection failed") {
        });

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productTypeService.updateById(StaticProductType.ID, updatedProductType));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productTypeRepository, times(1)).save(updatedProductType);
    }

    @Test
    void testDeleteById_Success() {
        boolean result = productTypeService.deleteById(StaticProductType.ID);

        verify(productTypeRepository).deleteById(StaticProductType.ID);
        assertTrue(result);
    }

    @Test
    void testDeleteById_AnyException() {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeRepository).deleteById(StaticProductType.ID);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productTypeService.deleteById(StaticProductType.ID));

        assertNotNull(exception);
        assertEquals("Database connection failed", exception.getMessage());
        verify(productTypeRepository, times(1)).deleteById(StaticProductType.ID);
    }
}