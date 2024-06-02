package es.obramat.technicalTest.application.controller;

import es.obramat.technicalTest.application.dto.OrderDTO;
import es.obramat.technicalTest.application.service.OrderService;
import es.obramat.technicalTest.domain.PageResult;
import es.obramat.technicalTest.infrastructure.filter.OrderFilter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Setter
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/search")
    public ResponseEntity<PageResult<OrderDTO>> getOrders(@RequestBody OrderFilter config) {
        PageResult<OrderDTO> orders = orderService.getOrdersDTO(config);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO result = orderService.createOrderDTO(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
        Optional<OrderDTO> orderDTOOptional = orderService.findDTOById(orderId);
        return orderDTOOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        Optional<OrderDTO> updatedOrderDTOOptional = orderService.updateOrderDTO(orderId, orderDTO);
        return updatedOrderDTOOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        boolean deleted = orderService.deleteOrderById(orderId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
