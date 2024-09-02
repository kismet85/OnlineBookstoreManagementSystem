package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import java.util.List;

public interface IOrderService {
    Order addOrder(Order order);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateOrder(Order order, Long id);
    void deleteOrder(Long id);
    List<Order> getOrdersByUserId(Long user_id);
}
