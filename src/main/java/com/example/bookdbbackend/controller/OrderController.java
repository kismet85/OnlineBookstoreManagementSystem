package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.controller.OrderItemDto;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.model.OrderItem;
import com.example.bookdbbackend.repository.OrderItemRepository;
import com.example.bookdbbackend.repository.OrderRepository;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto) {
        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order = orderRepository.save(order);
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(orderItemDto.getBook());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItemRepository.save(orderItem);
        }

        return ResponseEntity.ok(order);
    }
}