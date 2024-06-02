package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.application.dto.ProductDTO;
import es.obramat.technicalTest.application.mappers.ProductMapper;
import es.obramat.technicalTest.domain.model.Product;
import es.obramat.technicalTest.infrastructure.persistence.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Spy
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductsByName() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Product 1");
        Product product2 = new Product();
        product2.setName("Product 2");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findByNameContaining("Product")).thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByName("Product");

        // Assert
        assertEquals(products, result);
    }

    @Test
    void testGetProductsDTOByName() {
        // Arrange
        Product product1 = new Product();
        product1.setName("Silla");
        Product product2 = new Product();
        product2.setName("Sillón");
        Product product3 = new Product();
        product3.setName("Sillita");
        List<Product> products = List.of(product1, product2, product3);

        when(productRepository.findByNameContaining("lla")).thenReturn(products);

        when(productMapper.productToProductDTO(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(product.getName());
            return productDTO;
        });

        // Act
        List<ProductDTO> result = productService.getProductsDTOByName("lla");

        // Assert
        assertEquals(3, result.size());
        assertEquals("Silla", result.get(0).getName());
        assertEquals("Sillón", result.get(1).getName());
        assertEquals("Sillita", result.get(2).getName());
    }
}