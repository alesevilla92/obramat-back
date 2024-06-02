package es.obramat.technicalTest.application.service;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.dto.OrderProductDTO;
import es.obramat.technicalTest.application.mappers.OrderMapper;
import es.obramat.technicalTest.domain.PageResult;
import es.obramat.technicalTest.domain.model.Order;
import es.obramat.technicalTest.domain.model.OrderState;
import es.obramat.technicalTest.infrastructure.filter.OrderFilter;
import es.obramat.technicalTest.infrastructure.persistence.OrderProductRepository;
import es.obramat.technicalTest.infrastructure.persistence.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testGetOrdersDTO() {
        // Arrange
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setPageIndex(0);
        orderFilter.setPageSize(10);
        orderFilter.setSortBy("id");
        orderFilter.setSortDirection("asc");

        Order order1 = new Order();
        order1.setId(1L);
        order1.setCreationDate(LocalDateTime.now());
        order1.setTotalWithoutTax(100.0);
        order1.setState(OrderState.PENDING);
        order1.setProducts(new HashSet<>());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setCreationDate(LocalDateTime.now());
        order2.setTotalWithoutTax(200.0);
        order2.setState(OrderState.PAID);
        order2.setProducts(new HashSet<>());

        List<Order> orders = Arrays.asList(order1, order2);

        when(orderRepository.findAll((Specification<Order>) any(Specification.class), any(Pageable.class))).thenReturn(new PageImpl<>(orders));

        // Act
        PageResult<OrderDTO> result = orderService.getOrdersDTO(orderFilter);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());
        assertEquals(2, result.getSize());
    }

    @Test
    void testFindDTOById() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setCreationDate(LocalDateTime.now());
        order.setTotalWithoutTax(100.0);
        order.setState(OrderState.PENDING);
        order.setProducts(new HashSet<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(new OrderDTO());

        // Act
        Optional<OrderDTO> result = orderService.findDTOById(1L);

        // Assert
        assertTrue(result.isPresent());
    }

    @Test
    void testCreateOrderDTO() {
        // Arrange
        OrderProductDTO orderProductDTO = new OrderProductDTO();
        orderProductDTO.setProductId(1L);
        orderProductDTO.setQuantity(2.0);
        orderProductDTO.setPrice(50.0);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setProducts(List.of(orderProductDTO));
        orderDTO.setState(OrderState.PENDING.name());

        Order order = new Order();
        order.setId(1L);
        order.setCreationDate(LocalDateTime.now());
        order.setTotalWithoutTax(100.0);
        order.setState(OrderState.PENDING);
        order.setProducts(new HashSet<>());

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        // Act
        OrderDTO result = orderService.createOrderDTO(orderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(orderDTO, result);
    }

    @Test
    void testUpdateOrderDTO() {
        // Arrange
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCreationDate(LocalDateTime.now());
        existingOrder.setTotalWithoutTax(100.0);
        existingOrder.setState(OrderState.PENDING);
        existingOrder.setProducts(new HashSet<>());

        OrderProductDTO orderProductDTO = new OrderProductDTO();
        orderProductDTO.setProductId(1L);
        orderProductDTO.setQuantity(2.0);
        orderProductDTO.setPrice(50.0);

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setId(existingOrder.getId());
        updatedOrderDTO.setProducts(List.of(orderProductDTO));
        updatedOrderDTO.setState(OrderState.PAID.name());

        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);
        when(orderMapper.orderToOrderDTO(existingOrder)).thenReturn(updatedOrderDTO);

        // Act
        Optional<OrderDTO> result = orderService.updateOrderDTO(existingOrder.getId(), updatedOrderDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(OrderState.PAID.name(), result.get().getState());
    }

    @Test
    void testDeleteOrderById() {
        // Arrange
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setCreationDate(LocalDateTime.now());
        existingOrder.setTotalWithoutTax(100.0);
        existingOrder.setState(OrderState.PENDING);
        existingOrder.setProducts(new HashSet<>());

        when(orderRepository.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));

        // Act
        boolean result = orderService.deleteOrderById(existingOrder.getId());

        // Assert
        assertTrue(result);
        verify(orderRepository, times(1)).delete(existingOrder);
    }

}