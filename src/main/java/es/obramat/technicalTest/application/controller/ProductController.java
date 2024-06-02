package es.obramat.technicalTest.application.controller;

import es.obramat.technicalTest.application.dto.ProductDTO;
import es.obramat.technicalTest.application.service.ProductService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Setter
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getProducts(@RequestParam(value = "name", required = false) String name) {
        List<ProductDTO> products = productService.getProductsDTOByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
