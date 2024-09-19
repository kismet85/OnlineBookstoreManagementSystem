package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.OrderDto;
import com.example.bookdbbackend.dtos.OrderItemDto;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.model.OrderItem;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.OrderItemRepository;
import com.example.bookdbbackend.repository.OrderRepository;
import com.example.bookdbbackend.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final IUserService iUserService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;

    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, IUserService iUserService, JwtService jwtService, UserDetailsService userDetailsService, UserService userService, BookService bookService, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.iUserService = iUserService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.bookService = bookService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String token) {

        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByEmail(username);
        List<Order> orders = orderService.getOrdersByUserId(user.getUser_id());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto) {
        User user = userService.getUserById(orderDto.getUser_id());

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order = orderService.addOrder(order);


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