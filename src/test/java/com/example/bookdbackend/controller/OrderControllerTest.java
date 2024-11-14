package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.OrderController;
import com.example.bookdbbackend.dtos.OrderDto;
import com.example.bookdbbackend.dtos.OrderItemDto;
import com.example.bookdbbackend.dtos.OrderResponseDto;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.model.OrderItem;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.OrderItemRepository;
import com.example.bookdbbackend.service.BookService;
import com.example.bookdbbackend.service.IOrderService;

import com.example.bookdbbackend.service.JwtService;
import com.example.bookdbbackend.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class OrderControllerTest {

    @Mock
    private IOrderService iOrderService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;
    @Mock
    private BookService bookService;
    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderController orderController;

    private static final String VALID_TOKEN = "Bearer validToken";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String USER_EMAIL = "user@example.com";

    private User adminUser;
    private User regularUser;
    private OrderResponseDto orderResponseDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = new User();
        adminUser.setUser_id(1L);
        adminUser.setEmail(ADMIN_EMAIL);
        adminUser.setPassword("password");
        adminUser.setRole("ADMIN");
        adminUser.setFirst_name("Admin");
        adminUser.setLast_name("User");

        regularUser = new User();
        regularUser.setUser_id(2L);
        regularUser.setEmail(USER_EMAIL);
        regularUser.setPassword("password");
        regularUser.setRole("USER");
        regularUser.setFirst_name("Regular");
        regularUser.setLast_name("User");

        orderResponseDto = new OrderResponseDto();
    }

    @Test
    void getAllOrders_Success() {
        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(new Order());

        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);
        when(iOrderService.getAllOrders()).thenReturn(expectedOrders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders(VALID_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedOrders, response.getBody());
    }

    @Test
    void getAllOrders_InvalidToken() {
        when(jwtService.extractUsername("invalidToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        ResponseEntity<List<Order>> response = orderController.getAllOrders("Bearer invalidToken");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllOrders_NonAdminUser() {
        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);

        ResponseEntity<List<Order>> response = orderController.getAllOrders(VALID_TOKEN);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void getOrderById_Success() {
        // Prepare test data
        Long orderId = 1L;

        // Mock service responses
        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);
        when(iOrderService.getOrderById(orderId)).thenReturn(orderResponseDto);

        // Execute test
        ResponseEntity<OrderResponseDto> response = orderController.getOrderById(orderId, VALID_TOKEN);

        // Verify results
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(orderResponseDto, response.getBody());
    }

    @Test
    void getOrderById_InvalidToken() {
        // Prepare test data
        Long orderId = 1L;

        // Mock service responses
        when(jwtService.extractUsername("invalidToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(false);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        // Execute test
        ResponseEntity<OrderResponseDto> response = orderController.getOrderById(orderId, "Bearer invalidToken");

        // Verify results
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getOrderById_NonAdminUser() {
        // Prepare test data
        Long orderId = 1L;

        // Mock service responses
        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);

        // Execute test
        ResponseEntity<OrderResponseDto> response = orderController.getOrderById(orderId, VALID_TOKEN);

        // Verify results
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getOrderById_OrderNotFound() {
        // Prepare test data
        Long orderId = 999L; // Non-existent order ID

        // Mock service responses
        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);
        when(iOrderService.getOrderById(orderId)).thenReturn(null);

        // Execute test
        ResponseEntity<OrderResponseDto> response = orderController.getOrderById(orderId, VALID_TOKEN);

        // Verify results
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    void getOrders_Success() {
        List<Order> expectedOrders = new ArrayList<>();
        expectedOrders.add(new Order());

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);
        when(userService.getUserByEmail(USER_EMAIL)).thenReturn(regularUser);
        when(iOrderService.getOrdersByUserId(regularUser.getUser_id())).thenReturn(expectedOrders);

        ResponseEntity<List<Order>> response = orderController.getOrders(VALID_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedOrders, response.getBody());
    }

    @Test
    void getOrders_InvalidToken() {
        when(jwtService.extractUsername("invalidToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);

        ResponseEntity<List<Order>> response = orderController.getOrders("Bearer invalidToken");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addOrder_Success() {
        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(regularUser.getUser_id());
        orderDto.setOrderItems(new ArrayList<>());

        Order order = new Order();
        order.setOrder_id(1L);

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);
        when(userService.getUserById(orderDto.getUser_id())).thenReturn(regularUser);
        when(iOrderService.addOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<Order> response = orderController.addOrder(orderDto, VALID_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(order, response.getBody());
    }

    @Test
    void addOrder_ValidOrderItems() {
        // Arrange
        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(regularUser.getUser_id());

        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setBook_id(1L);
        orderItemDto.setQuantity(1);
        orderItemDto.setPrice(BigDecimal.valueOf(100.0));
        orderItemDtos.add(orderItemDto);
        orderDto.setOrderItems(orderItemDtos);

        Order order = new Order();
        order.setOrder_id(1L);

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);
        when(userService.getUserById(orderDto.getUser_id())).thenReturn(regularUser);
        when(bookService.getBookById(anyLong())).thenReturn(new Book());
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(new OrderItem());
        when(iOrderService.addOrder(any(Order.class))).thenReturn(order);

        // Act
        ResponseEntity<Order> response = orderController.addOrder(orderDto, VALID_TOKEN);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookService, times(1)).getBookById(anyLong());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(iOrderService, times(1)).addOrder(any(Order.class));
    }

    @Test
    void addOrder_InvalidToken() {
        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(regularUser.getUser_id());
        orderDto.setOrderItems(new ArrayList<>());

        when(jwtService.extractUsername("invalidToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);

        ResponseEntity<Order> response = orderController.addOrder(orderDto, "Bearer invalidToken");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addOrder_InvalidUserId() {
        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(999L); // Non-existent user ID
        orderDto.setOrderItems(new ArrayList<>());

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);
        when(userService.getUserById(orderDto.getUser_id())).thenReturn(null);

        ResponseEntity<Order> response = orderController.addOrder(orderDto, VALID_TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addOrder_OrderCreationFailure() {
        OrderDto orderDto = new OrderDto();
        orderDto.setUser_id(regularUser.getUser_id());
        orderDto.setOrderItems(new ArrayList<>());

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);
        when(userService.getUserById(orderDto.getUser_id())).thenReturn(regularUser);
        when(iOrderService.addOrder(any(Order.class))).thenThrow(new RuntimeException("Order creation failed"));

        ResponseEntity<Order> response = orderController.addOrder(orderDto, VALID_TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteOrder_Success() {
        Long orderId = 1L;

        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        ResponseEntity<String> response = orderController.deleteOrder(orderId, VALID_TOKEN);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted order with id: " + orderId, response.getBody());
    }

    @Test
    void deleteOrder_InvalidToken() {
        Long orderId = 1L;

        when(jwtService.extractUsername("invalidToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(false);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        ResponseEntity<String> response = orderController.deleteOrder(orderId, "Bearer invalidToken");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }

    @Test
    void deleteOrder_NonAdminUser() {
        Long orderId = 1L;

        when(jwtService.extractUsername("validToken")).thenReturn(USER_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(USER_EMAIL)).thenReturn(regularUser);

        ResponseEntity<String> response = orderController.deleteOrder(orderId, VALID_TOKEN);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody());
    }

    @Test
    void deleteOrder_OrderNotFound() {
        Long orderId = 999L; // Non-existent order ID

        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        doThrow(new EntityNotFoundException("Order not found")).when(iOrderService).deleteOrder(orderId);

        ResponseEntity<String> response = orderController.deleteOrder(orderId, VALID_TOKEN);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Order not found", response.getBody());
    }

    @Test
    void deleteOrder_DeletionFailure() {
        Long orderId = 1L;

        when(jwtService.extractUsername("validToken")).thenReturn(ADMIN_EMAIL);
        when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(userDetailsService.loadUserByUsername(ADMIN_EMAIL)).thenReturn(adminUser);

        doThrow(new RuntimeException("Deletion failed")).when(iOrderService).deleteOrder(orderId);

        ResponseEntity<String> response = orderController.deleteOrder(orderId, VALID_TOKEN);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while deleting the order", response.getBody());
    }
}