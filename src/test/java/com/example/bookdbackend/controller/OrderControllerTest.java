package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.OrderController;
import com.example.bookdbbackend.service.IOrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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