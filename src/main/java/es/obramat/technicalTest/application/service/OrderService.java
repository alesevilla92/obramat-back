package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.dto.OrderProductDTO;
import es.obramat.technicalTest.application.filter.OrderSpecificationBuilder;
import es.obramat.technicalTest.application.mappers.OrderMapper;
import es.obramat.technicalTest.domain.PageResult;
import es.obramat.technicalTest.domain.model.Order;
import es.obramat.technicalTest.domain.model.OrderProduct;
import es.obramat.technicalTest.domain.model.OrderState;
import es.obramat.technicalTest.infrastructure.filter.OrderFilter;
import es.obramat.technicalTest.infrastructure.persistence.OrderProductRepository;
import es.obramat.technicalTest.infrastructure.persistence.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderMapper orderMapper;

    public PageResult<OrderDTO> getOrdersDTO(OrderFilter config) {
        Specification<Order> specification = new OrderSpecificationBuilder(config).buildSpecification();

        Pageable pageable = PageRequest.of(config.getPageIndex(), config.getPageSize(),
                Sort.by(config.getSortDirection().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        config.getSortBy()));
        Page<Order> orders = orderRepository.findAll(specification, pageable);

        List<OrderDTO> elements = orders.getContent().stream()
                .map(this::mapOrderToOrderDTO).collect(Collectors.toList());
        return new PageResult<>(elements, orders.getTotalElements(), orders.getTotalPages(), orders.getNumber(),
                orders.getSize());
    }

    public Optional<OrderDTO> findDTOById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(this::mapOrderToOrderDTO);
    }

    public OrderDTO createOrderDTO(OrderDTO orderDTO) {
        Order order = new Order();
        order.setCreationDate(LocalDateTime.now());
        order.setState(OrderState.valueOf(orderDTO.getState()));

        Set<OrderProduct> orderProducts = orderDTO.getProducts().stream()
                .map(this::mapOrderProductDTOToOrderProduct)
                .collect(Collectors.toSet());

        order = orderRepository.save(order);

        Long orderId = order.getId();
        orderProducts.forEach(op -> op.setOrderId(orderId));

        order.setProducts(orderProducts.stream().map(orderProductRepository::save).collect(Collectors.toSet()));

        double totalWithoutTax = orderProducts.stream()
                .mapToDouble(op -> op.getQuantity() * op.getPrice())
                .sum();

        order.setTotalWithoutTax(totalWithoutTax);

        order = orderRepository.save(order);

        return mapOrderToOrderDTO(order);
    }

    public Optional<OrderDTO> updateOrderDTO(Long orderId, OrderDTO orderDTO) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        return orderOptional.map(order -> {
            Set<OrderProduct> updatedOrderProducts = orderDTO.getProducts().stream()
                    .map(this::mapOrderProductDTOToOrderProduct)
                    .collect(Collectors.toSet());

            // Delete non existing products in the new order
            order.getProducts().stream()
                    .filter(op -> !updatedOrderProducts.contains(op))
                    .forEach(orderProductRepository::delete);

            // Update products that already exist in the new order
            updatedOrderProducts.forEach(op -> {
                Optional<OrderProduct> existingOrderProductOptional = orderProductRepository
                        .findByOrderIdAndProductId(orderId, op.getProductId());
                if (existingOrderProductOptional.isPresent()) {
                    OrderProduct existingOrderProduct = existingOrderProductOptional.get();
                    existingOrderProduct.setQuantity(op.getQuantity());
                    orderProductRepository.save(existingOrderProduct);
                } else {
                    op.setOrderId(orderId);
                    orderProductRepository.save(op);
                }
            });

            order.setProducts(updatedOrderProducts);

            double totalWithoutTax = updatedOrderProducts.stream()
                    .mapToDouble(op -> op.getQuantity() * op.getPrice())
                    .sum();

            order.setTotalWithoutTax(totalWithoutTax);
            order.setState(OrderState.valueOf(orderDTO.getState()));

            Order savedOrder = orderRepository.save(order);

            return mapOrderToOrderDTO(savedOrder);
        });
    }

    public boolean deleteOrderById(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        orderOptional.ifPresent(order -> {
            orderProductRepository.findAllByOrderId(order.getId()).forEach(orderProductRepository::delete);
            orderRepository.delete(order);
        });
        return orderOptional.isPresent();
    }

    private OrderProduct mapOrderProductDTOToOrderProduct(OrderProductDTO orderProductDTO) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProductId(orderProductDTO.getProductId());
        orderProduct.setQuantity(orderProductDTO.getQuantity());
        orderProduct.setPrice(orderProductDTO.getPrice());
        return orderProduct;
    }

    private OrderDTO mapOrderToOrderDTO(Order order) {
        return orderMapper.orderToOrderDTO(order);
    }

}