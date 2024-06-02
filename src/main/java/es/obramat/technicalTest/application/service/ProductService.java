package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.application.dto.ProductDTO;
import es.obramat.technicalTest.application.mappers.ProductMapper;
import es.obramat.technicalTest.domain.model.Product;
import es.obramat.technicalTest.infrastructure.persistence.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public List<ProductDTO> getProductsDTOByName(String name) {
        return productRepository.findByNameContaining(name).stream().map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }

}
