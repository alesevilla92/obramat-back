package es.obramat.technicalTest.application.mappers;

import es.obramat.technicalTest.application.dto.ProductDTO;
import es.obramat.technicalTest.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    ProductDTO productToProductDTO(Product product);
}
