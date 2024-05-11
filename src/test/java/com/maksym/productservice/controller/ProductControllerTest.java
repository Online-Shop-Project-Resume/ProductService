package com.maksym.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.productservice.dto.request.ProductDtoRequest;
import com.maksym.productservice.dto.response.ProductDtoResponse;
import com.maksym.productservice.model.Product;
import com.maksym.productservice.exception.EntityNotFoundException;
import com.maksym.productservice.service.ProductService;
import com.maksym.productservice.staticObject.StaticProduct;
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

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private final String DOCUMENTATION_URI = "http://swagger_documentation";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ProductDtoRequest productRequest = StaticProduct.productDtoRequest1();
    private final Product productModel = StaticProduct.product1(); 
    private final ProductDtoResponse productResponse = StaticProduct.productDtoResponse1();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(productService);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler(DOCUMENTATION_URI))
                .build();
    }

    @Test
    void testCreate_Success_ShouldReturnCreated() throws Exception {
        when(productService.create(any(Product.class))).thenReturn(productModel);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.type.id").value(productResponse.getType().getId()))
                .andExpect(jsonPath("$.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.rating").value(productResponse.getRating()));
    }

    @Test
    void testCreate_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreate_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productService.create(any(Product.class))).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Product not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testCreate_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productService).create(any(Product.class));

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_Success_ShouldReturnOk() throws Exception {
        when(productService.getById(StaticProduct.ID)).thenReturn(productModel);

        mockMvc.perform(get("/api/product/{id}", StaticProduct.ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.type.id").value(productResponse.getType().getId()))
                .andExpect(jsonPath("$.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.rating").value(productResponse.getRating()));
    }

    @Test
    void testGetById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productService.getById(any())).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(get("/api/product/"+StaticProduct.ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Product not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testGetById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productService).getById(any());

        mockMvc.perform(get("/api/product/"+StaticProduct.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testGetAll_Success_ShouldReturnOk() throws Exception {
        List<Product> productList = Arrays.asList(productModel, StaticProduct.product1());
        Page<Product> productPage = new PageImpl<>(productList);
        Pageable pageable = Pageable.unpaged();
        when(productService.getAll(pageable)).thenReturn(productPage);

        mockMvc.perform(get("/api/product/"))
                .andExpect(status().isOk())
		        .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(productResponse.getId()))
                .andExpect(jsonPath("$.[1].id").value(StaticProduct.productDtoResponse2().getId()));
    }

    @Test
    void testGetAll_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productService).getAll(any(Pageable.class));

        mockMvc.perform(get("/api/product/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }


    @Test
    void testUpdateById_Success_ShouldReturnOk() throws Exception {
        when(productService.updateById(any(), any(Product.class))).thenReturn(productModel);

        mockMvc.perform(put("/api/product/"+StaticProduct.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.type.id").value(productResponse.getType().getId()))
                .andExpect(jsonPath("$.description").value(productResponse.getDescription()))
                .andExpect(jsonPath("$.price").value(productResponse.getPrice()))
                .andExpect(jsonPath("$.rating").value(productResponse.getRating()));
    }

    @Test
    void testUpdateById_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/api/product/"+StaticProduct.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateById_EntityNotFoundException_ShouldReturnNotFound() throws Exception {
        when(productService.updateById(any(), any(Product.class))).thenThrow(new EntityNotFoundException("Product not found"));

        mockMvc.perform(put("/api/product/"+StaticProduct.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage").value("Product not found"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testUpdateById_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productService).updateById(any(), any(Product.class));

        mockMvc.perform(put("/api/product/"+StaticProduct.ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }

    @Test
    void testDelete_Success_ShouldReturnNoContent() throws Exception {
        when(productService.deleteById(StaticProduct.ID)).thenReturn(true);

        mockMvc.perform(delete("/api/product/"+StaticProduct.ID))
                .andExpect(status().isNoContent());
    }
	
    @Test
    void testDelete_AnyException_ShouldReturnBadRequest() throws Exception {
        doThrow(new DataAccessException("Database connection failed") {}).when(productService).deleteById(StaticProduct.ID);

        mockMvc.perform(delete("/api/product/"+StaticProduct.ID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Database connection failed"))
                .andExpect(jsonPath("$.documentationUri").value(DOCUMENTATION_URI));
    }
}