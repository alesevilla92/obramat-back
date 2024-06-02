package es.obramat.technicalTest.application.mappers;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.dto.OrderProductDTO;
import es.obramat.technicalTest.domain.model.Order;
import es.obramat.technicalTest.domain.model.OrderProduct;
import es.obramat.technicalTest.domain.model.Product;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-02T22:33:14+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO orderToOrderDTO(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId( order.getId() );
        if ( order.getCreationDate() != null ) {
            orderDTO.setCreationTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( order.getCreationDate() ) );
        }
        orderDTO.setTotalWithoutTax( order.getTotalWithoutTax() );
        orderDTO.setTotalWithTax( order.getTotalWithTax() );
        if ( order.getState() != null ) {
            orderDTO.setState( order.getState().name() );
        }
        orderDTO.setProducts( orderProductSetToOrderProductDTOList( order.getProducts() ) );

        return orderDTO;
    }

    @Override
    public OrderProductDTO orderProductToOrderProductDTO(OrderProduct orderProduct) {
        if ( orderProduct == null ) {
            return null;
        }

        OrderProductDTO orderProductDTO = new OrderProductDTO();

        orderProductDTO.setProductId( orderProductProductId( orderProduct ) );
        orderProductDTO.setName( orderProductProductName( orderProduct ) );
        orderProductDTO.setQuantity( orderProduct.getQuantity() );
        orderProductDTO.setPrice( orderProduct.getPrice() );

        return orderProductDTO;
    }

    protected List<OrderProductDTO> orderProductSetToOrderProductDTOList(Set<OrderProduct> set) {
        if ( set == null ) {
            return null;
        }

        List<OrderProductDTO> list = new ArrayList<OrderProductDTO>( set.size() );
        for ( OrderProduct orderProduct : set ) {
            list.add( orderProductToOrderProductDTO( orderProduct ) );
        }

        return list;
    }

    private Long orderProductProductId(OrderProduct orderProduct) {
        if ( orderProduct == null ) {
            return null;
        }
        Product product = orderProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        Long id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String orderProductProductName(OrderProduct orderProduct) {
        if ( orderProduct == null ) {
            return null;
        }
        Product product = orderProduct.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
