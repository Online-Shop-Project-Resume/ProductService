package com.maksym.productservice.service;

import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductTypeServiceTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    @Test
    void testAddProductType() {
        // Given
        ProductTypeRequest request = new ProductTypeRequest(/* Add necessary details */);
        ProductType productType = new ProductType(/* Add necessary details */);
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        // When
        ProductType addedProductType = productTypeService.add(request);

        // Then
        assertNotNull(addedProductType);
        // Add additional assertions as needed
    }

    @Test
    void testDeleteProductType() {
        // Given
        Long productTypeId = 1L;

        // When
        boolean deleted = productTypeService.deleteById(productTypeId);

        // Then
        assertTrue(deleted);
        verify(productTypeRepository, times(1)).deleteById(productTypeId);
    }

    @Test
    void testUpdateProductType() {
        // Given
        Long productTypeId = 1L;
        ProductTypeRequest request = new ProductTypeRequest(/* Add necessary details */);
        ProductType productType = new ProductType(/* Add necessary details */);
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.of(productType));
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(productType);

        // When
        ProductType updatedProductType = productTypeService.update(productTypeId, request);

        // Then
        assertNotNull(updatedProductType);
        // Add additional assertions as needed
    }

    @Test
    void testGetAllProductTypes() {
        // Given
        List<ProductType> productTypeList = new ArrayList<>();
        // Add some mock product types to the list

        when(productTypeRepository.findAll()).thenReturn(productTypeList);

        // When
        List<ProductType> retrievedProductTypes = productTypeService.getAll();

        // Then
        assertNotNull(retrievedProductTypes);
        // Add additional assertions as needed
    }

    @Test
    void testGetProductTypeById() {
        // Given
        Long productTypeId = 1L;
        ProductType expectedProductType = new ProductType(/* Add necessary details */);
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.of(expectedProductType));

        // When
        ProductType retrievedProductType = productTypeService.getById(productTypeId);

        // Then
        assertNotNull(retrievedProductType);
        assertEquals(expectedProductType, retrievedProductType);
    }

    @Test
    void testGetProductTypeById_NotFound() {
        // Given
        Long productTypeId = 1L;
        when(productTypeRepository.findById(productTypeId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> productTypeService.getById(productTypeId));
    }
}
