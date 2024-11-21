package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.OrderDto;
import com.example.bookdbbackend.dtos.OrderItemDto;
import com.example.bookdbbackend.dtos.OrderResponseDto;
import com.example.bookdbbackend.model.*;
import com.example.bookdbbackend.repository.InventoryRepository;
import com.example.bookdbbackend.repository.OrderItemRepository;
import com.example.bookdbbackend.repository.OrderRepository;
import com.example.bookdbbackend.service.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling order-related requests.
 */
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
    private final IOrderService iOrderService;
    private final InventoryService inventoryService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * Constructor for OrderController.
     *
     * @param orderRepository the order repository
     * @param orderItemRepository the order item repository
     * @param iUserService the user service
     * @param jwtService the JWT service
     * @param userDetailsService the user details service
     * @param userService the user service
     * @param bookService the book service
     * @param iOrderService the order service
     * @param inventoryService the inventory service
     */
    public OrderController(OrderRepository orderRepository, OrderItemRepository orderItemRepository, IUserService iUserService, JwtService jwtService, UserDetailsService userDetailsService, UserService userService, BookService bookService, IOrderService iOrderService, InventoryService inventoryService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.iUserService = iUserService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.bookService = bookService;
        this.iOrderService = iOrderService;
        this.inventoryService = inventoryService;
    }

    /**
     * Endpoint for getting all orders.
     *
     * @param token the JWT token from the Authorization header
     * @return a list of all orders
     */
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

        return new ResponseEntity<>(iOrderService.getAllOrders(), HttpStatus.OK);
    }

    /**
     * Endpoint for getting an order by ID.
     *
     * @param id the ID of the order
     * @param token the JWT token from the Authorization header
     * @return the order with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
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

        OrderResponseDto order = iOrderService.getOrderById(id);

        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Endpoint for getting orders of the authenticated user.
     *
     * @param token the JWT token from the Authorization header
     * @return a list of orders of the authenticated user
     */
    @GetMapping("/me")
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByEmail(username);
        List<Order> orders = iOrderService.getOrdersByUserId(user.getUser_id());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Endpoint for adding a new order.
     *
     * @param orderDto the order data transfer object
     * @param token the JWT token from the Authorization header
     * @return the created order
     */
    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        logger.info("User ID: {}", orderDto.getUser_id());
        User user = userService.getUserById(orderDto.getUser_id());
        if (user == null) {
            logger.error("User not found for ID: {}", orderDto.getUser_id());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("User email: {}", user.getEmail());

        logger.info("Creating new order for user ID: {}", orderDto.getUser_id());
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());

        try {
            logger.info("Saving order to database...");
            order = iOrderService.addOrder(order);
        } catch (Exception e) {
            logger.error("Error while saving order to database", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

    /**
     * Endpoint for deleting an order by ID.
     *
     * @param id the ID of the order
     * @param token the JWT token from the Authorization header
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }

        try {
            iOrderService.deleteOrder(id);
            return new ResponseEntity<>("Successfully deleted order with id: " + id, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while deleting the order", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}