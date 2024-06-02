package es.obramat.technicalTest.application.controller;

import es.obramat.technicalTest.application.dto.ProductDTO;
import es.obramat.technicalTest.application.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    private final ProductService productService = mock(ProductService.class);
    private final ProductController productController = new ProductController();

    @BeforeEach
    void setUp() {
        productController.setProductService(productService);
    }

    @Test
    void testGetProducts() {
        // Arrange
        String productName = "Chair";
        List<ProductDTO> products = Collections.singletonList(new ProductDTO());

        when(productService.getProductsDTOByName(productName)).thenReturn(products);

        // Act
        ResponseEntity<List<ProductDTO>> response = productController.getProducts(productName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }
}