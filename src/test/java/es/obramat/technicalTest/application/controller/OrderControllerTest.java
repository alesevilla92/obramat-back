package es.obramat.technicalTest.application.controller;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.service.OrderService;
import es.obramat.technicalTest.domain.PageResult;
import es.obramat.technicalTest.infrastructure.filter.OrderFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {

    private final OrderService orderService = mock(OrderService.class);
    private final OrderController orderController = new OrderController();

    @BeforeEach
    void setUp() {
        orderController.setOrderService(orderService);
    }

    @Test
    void testGetOrders() {
        // Arrange
        OrderFilter orderFilter = new OrderFilter();
        PageResult<OrderDTO> orders = new PageResult<>(null, 0, 0, 0, 0);

        when(orderService.getOrdersDTO(orderFilter)).thenReturn(orders);

        // Act
        ResponseEntity<PageResult<OrderDTO>> response = orderController.getOrders(orderFilter);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orders, response.getBody());
    }

    @Test
    void testCreateOrder() {
        // Arrange
        OrderDTO orderDTO = new OrderDTO();
        OrderDTO result = new OrderDTO();

        when(orderService.createOrderDTO(orderDTO)).thenReturn(result);

        // Act
        ResponseEntity<OrderDTO> response = orderController.createOrder(orderDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(result, response.getBody());
    }

    @Test
    void testGetOrder() {
        // Arrange
        Long orderId = 1L;
        Optional<OrderDTO> orderDTOOptional = Optional.of(new OrderDTO());

        when(orderService.findDTOById(orderId)).thenReturn(orderDTOOptional);

        // Act
        ResponseEntity<OrderDTO> response = orderController.getOrder(orderId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderDTOOptional.get(), response.getBody());
    }

    @Test
    void testUpdateOrder() {
        // Arrange
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        Optional<OrderDTO> updatedOrderDTOOptional = Optional.of(new OrderDTO());

        when(orderService.updateOrderDTO(orderId, orderDTO)).thenReturn(updatedOrderDTOOptional);

        // Act
        ResponseEntity<OrderDTO> response = orderController.updateOrder(orderId, orderDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedOrderDTOOptional.get(), response.getBody());
    }

    @Test
    void testDeleteOrder() {
        // Arrange
        Long orderId = 1L;
        boolean deleted = true;

        when(orderService.deleteOrderById(orderId)).thenReturn(deleted);

        // Act
        ResponseEntity<Void> response = orderController.deleteOrder(orderId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}