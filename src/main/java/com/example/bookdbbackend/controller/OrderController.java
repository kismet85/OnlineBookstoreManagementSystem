package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.OrderDto;
import com.example.bookdbbackend.dtos.OrderItemDto;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.model.OrderItem;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.OrderItemRepository;
import com.example.bookdbbackend.repository.OrderRepository;
import com.example.bookdbbackend.service.BookService;
import com.example.bookdbbackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final UserService userService;

    private final BookService bookService;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserService userService, BookService bookService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto) {
        User user = userService.getUserById(orderDto.getUser_id());

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order = orderRepository.save(order);


        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getOrderItems()) {
            Book book = bookService.getBookById(orderItemDto.getBook_id());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItemRepository.save(orderItem);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        return ResponseEntity.ok(order);
    }
}