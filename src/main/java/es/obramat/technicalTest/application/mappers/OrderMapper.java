package es.obramat.technicalTest.application.mappers;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.dto.OrderProductDTO;
import es.obramat.technicalTest.domain.model.Order;
import es.obramat.technicalTest.domain.model.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "creationDate", target = "creationTime")
    @Mapping(source = "totalWithoutTax", target = "totalWithoutTax")
    @Mapping(source = "totalWithTax", target = "totalWithTax")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "products", target = "products", qualifiedByName = "orderProductMapper")
    OrderDTO orderToOrderDTO(Order order);

    @Named("orderProductMapper")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    OrderProductDTO orderProductToOrderProductDTO(OrderProduct orderProduct);

}
