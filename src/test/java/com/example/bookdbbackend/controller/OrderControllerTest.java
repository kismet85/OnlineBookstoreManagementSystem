package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.service.IOrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @InjectMocks
    private OrderController orderController;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getAllOrders() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void getOrders() {
    }

    @Test
    void addOrder() {
    }

    @Test
    void deleteOrder() {
    }
}