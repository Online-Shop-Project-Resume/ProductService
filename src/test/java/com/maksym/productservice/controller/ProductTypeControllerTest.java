package com.maksym.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.productservice.controllers.ProductTypeController;
import com.maksym.productservice.dto.ProductTypeRequest;
import com.maksym.productservice.exception.GlobalExceptionHandler;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.service.ProductTypeServiceImpl;
import com.maksym.productservice.staticObject.StaticProductType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductTypeControllerTest {

    @Mock
    private ProductTypeServiceImpl productTypeService;

    @InjectMocks
    private ProductTypeController productTypeController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Long id = 1L;
    private MockMvc mockMvc;
    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(productTypeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testAddProductType() throws Exception {
        ProductTypeRequest request = StaticProductType.productTypeRequest1();
        ProductType expectedProductType = StaticProductType.productType1();
        when(productTypeService.add(any(ProductTypeRequest.class))).thenReturn(expectedProductType);

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void testGetAllProductTypes() throws Exception {
        ProductType productType = StaticProductType.productType1();
        when(productTypeService.getAll()).thenReturn(Collections.singletonList(productType));

        mockMvc.perform(get("/api/product-type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    // Add similar tests for other controller methods

    @Test
    void testHandleEntityNotFoundException() throws Exception {
        String errorMessage = "ProductType not found";
        when(productTypeService.add(any(ProductTypeRequest.class))).thenThrow(new EntityNotFoundException(errorMessage));

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(errorMessage));
    }

    @Test
    void testHandleConstraintViolationException() throws Exception {
        when(productTypeService.add(any(ProductTypeRequest.class))).thenThrow(new ConstraintViolationException(new HashSet<>()));

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetById() throws Exception {
        ProductType productType = StaticProductType.productType1();
        when(productTypeService.getById(productType.getId())).thenReturn(productType);

        mockMvc.perform(get("/api/product-type/{id}", productType.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productType.getId()))
                .andExpect(jsonPath("$.name").value(productType.getName()));
    }

    @Test
    void testDeleteById() throws Exception {
        when(productTypeService.deleteById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/product-type/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateProductType_Success() throws Exception {
        // Mock successful update
        ProductTypeRequest request = StaticProductType.productTypeRequest1();
        ProductType updatedProductType = StaticProductType.productType1();
        when(productTypeService.update(id, request)).thenReturn(updatedProductType);

        // Perform PUT request
        mockMvc.perform(put("/api/product-type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"name\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(updatedProductType.getId()))
                .andExpect(jsonPath("$.name").value(updatedProductType.getName()));
    }

    @Test
    public void testUpdateProductType_EntityNotFoundException() throws Exception {
        // Mock EntityNotFoundException
        ProductTypeRequest request = StaticProductType.productTypeRequest1();
        when(productTypeService.update(id, request)).thenThrow(new EntityNotFoundException("ProductType with id: 1 is not found"));

        // Perform PUT request
        mockMvc.perform(put("/api/product-type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \""+request.getName()+"\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ProductType with id: 1 is not found"));
    }

    @Test
    public void testUpdateProductType_ConstraintViolationException() throws Exception {
        // Mock ConstraintViolationException
        ProductTypeRequest request = StaticProductType.productTypeRequestInvalid();
        when(productTypeService.update(id, request)).thenThrow(new ConstraintViolationException(new HashSet<>()));

        // Perform PUT request
        mockMvc.perform(put("/api/product-type/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \""+request.getName()+"\"}") // Empty name
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
