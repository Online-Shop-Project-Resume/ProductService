package com.maksym.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.productservice.dto.request.ProductTypeDtoRequest;
import com.maksym.productservice.dto.response.ProductTypeDtoResponse;
import com.maksym.productservice.model.ProductType;
import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.service.ProductTypeService;
import com.maksym.productservice.staticObject.StaticProductType;
import com.maksym.productservice.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductTypeService productTypeService;

    private final String DOCUMENTATION_URI = "http://swagger_documentation";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductTypeDtoRequest productTypeRequest = StaticProductType.productTypeDtoRequest1();
    private final ProductType productTypeModel = StaticProductType.productType1(); 
    private final ProductTypeDtoResponse productTypeResponse = StaticProductType.productTypeDtoResponse1();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductTypeController productTypeController = new ProductTypeController(productTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(productTypeController)
                .setControllerAdvice(new GlobalExceptionHandler(DOCUMENTATION_URI))
                .build();
    }

    @Test
    void testCreate_Success_ShouldReturnCreated() throws Exception {
        when(productTypeService.create(any(ProductType.class))).thenReturn(productTypeModel);

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productTypeResponse.getId()))
                .andExpect(jsonPath("$.name").value(productTypeResponse.getName()));
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productTypeService.create(any(ProductType.class))).thenThrow(new EntityNotFoundException("ProductType not found"));

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("ProductType not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testCreate_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeService).create(any(ProductType.class));

        mockMvc.perform(post("/api/product-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_Success_ShouldReturnOk() throws Exception {
        when(productTypeService.getById(StaticProductType.ID)).thenReturn(productTypeModel);

        mockMvc.perform(get("/api/product-type/{id}", StaticProductType.ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productTypeResponse.getId()))
                .andExpect(jsonPath("$.name").value(productTypeResponse.getName()));
    }

    @Test
    void testGetById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productTypeService.getById(any())).thenThrow(new EntityNotFoundException("ProductType not found"));

        mockMvc.perform(get("/api/product-type/"+StaticProductType.ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("ProductType not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeService).getById(any());

        mockMvc.perform(get("/api/product-type/"+StaticProductType.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testGetAll_Success_ShouldReturnOk() throws Exception {
        List<ProductType> productTypeList = Arrays.asList(productTypeModel, StaticProductType.productType1());
        Page<ProductType> productTypePage = new PageImpl<>(productTypeList);
        Pageable pageable = Pageable.unpaged();
        when(productTypeService.getAll(pageable)).thenReturn(productTypePage);

        mockMvc.perform(get("/api/product-type/"))
                .andExpect(status().isOk())
		        .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(productTypeResponse.getId()))
                .andExpect(jsonPath("$.[1].id").value(StaticProductType.productTypeDtoResponse2().getId()));
    }

    @Test
    void testGetAll_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeService).getAll(any(Pageable.class));

        mockMvc.perform(get("/api/product-type/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testUpdateById_Success_ShouldReturnOk() throws Exception {
        when(productTypeService.updateById(any(), any(ProductType.class))).thenReturn(productTypeModel);

        mockMvc.perform(put("/api/product-type/"+StaticProductType.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productTypeResponse.getId()))
                .andExpect(jsonPath("$.name").value(productTypeResponse.getName()));
    }

    @Test
    void testUpdateById_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/product-type/"+StaticProductType.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productTypeService.updateById(any(), any(ProductType.class))).thenThrow(new EntityNotFoundException("ProductType not found"));

        mockMvc.perform(put("/api/product-type/"+StaticProductType.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("ProductType not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testUpdateById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeService).updateById(any(), any(ProductType.class));

        mockMvc.perform(put("/api/product-type/"+StaticProductType.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productTypeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testDelete_Success_ShouldReturnNoContent() throws Exception {
        when(productTypeService.deleteById(StaticProductType.ID)).thenReturn(true);

        mockMvc.perform(delete("/api/product-type/"+StaticProductType.ID))
                .andExpect(status().isNoContent());
    }
	
    @Test
    void testDelete_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productTypeService).deleteById(StaticProductType.ID);

        mockMvc.perform(delete("/api/product-type/"+StaticProductType.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }
}