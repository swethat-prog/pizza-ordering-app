package com.example.pizza.test;

import com.example.pizza.controller.ProductController;
import com.example.pizza.entity.Product;
import com.example.pizza.security.CustomUserDetailsService;
import com.example.pizza.security.JwtProvider;
import com.example.pizza.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtProvider jwtProvider;

    @Test
    void testGetAllProducts() throws Exception {
        Product product = new Product(UUID.randomUUID(), "Margherita", "Cheese Pizza", BigDecimal.valueOf(8.99), true);
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product));

        // Mock JWT token to simulate authentication
        String token = "mocked-jwt-token";
        Mockito.when(jwtProvider.generateToken(Mockito.anyString())).thenReturn(token);

        // Simulate an authenticated request by adding the Authorization header with the token
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token)) // Include Authorization header with JWT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Margherita"));
    }

    @Test
    void testCreateProduct() throws Exception {
        String requestBody = """
            {
                "name": "Veggie Pizza",
                "description": "Healthy veggies",
                "price": 10.99,
                "available": true
            }
        """;

        // Mock the product creation response
        Mockito.when(productService.createProduct(Mockito.any())).thenReturn(null); // Mock the behavior

        // Mock JWT token to simulate authentication
        String token = "mocked-jwt-token";
        Mockito.when(jwtProvider.generateToken(Mockito.anyString())).thenReturn(token);

        // Simulate an authenticated request by adding the Authorization header with JWT
        mockMvc.perform(post("/api/products")
                        .header("Authorization", "Bearer " + token) // Include Authorization header with JWT
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
}
