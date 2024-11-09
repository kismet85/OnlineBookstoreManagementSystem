package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.OrderResponseDto;
import com.example.bookdbbackend.exception.OrderAlreadyExistsException;
import com.example.bookdbbackend.exception.OrderNotFoundException;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrder() {
        Order order = new Order();
        order.setOrder_id(1L);

        when(orderRepository.findById(order.getOrder_id())).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);

        Order savedOrder = orderService.addOrder(order);

        assertEquals(order, savedOrder);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testAddOrderAlreadyExists() {
        Order order = new Order();
        order.setOrder_id(1L);

        when(orderRepository.findById(order.getOrder_id())).thenReturn(Optional.of(order));

        assertThrows(OrderAlreadyExistsException.class, () -> orderService.addOrder(order));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(new Order(), new Order());

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(orders, result);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
    }

    @Test
    void testGetOrderByIdNotFound() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(id));
        verify(orderRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        order.setOrder_id(1L);

        when(orderRepository.findById(order.getOrder_id())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updatedOrder = orderService.updateOrder(order, order.getOrder_id());

        assertEquals(order, updatedOrder);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrderNotFound() {
        Order order = new Order();
        order.setOrder_id(1L);

        when(orderRepository.findById(order.getOrder_id())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(order, order.getOrder_id()));
        verify(orderRepository, never()).save(order);
    }

    @Test
    void testDeleteOrder() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.of(new Order()));

        orderService.deleteOrder(id);

        verify(orderRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteOrderNotFound() {
        Long id = 1L;

        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(id));
        verify(orderRepository, never()).deleteById(id);
    }

    @Test
    void testGetOrdersByUserId() {
        Long userId = 1L;
        List<Order> orders = List.of(new Order(), new Order());

        when(orderRepository.findOrdersByUserId(userId)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId(userId);

        assertEquals(orders, result);
        verify(orderRepository, times(1)).findOrdersByUserId(userId);
    }
}