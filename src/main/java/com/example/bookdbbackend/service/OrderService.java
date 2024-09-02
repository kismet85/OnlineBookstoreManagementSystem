package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.OrderAlreadyExistsException;
import com.example.bookdbbackend.exception.OrderAlreadyExistsException;
import com.example.bookdbbackend.exception.OrderNotFoundException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;
    @Override
    public Order addOrder(Order order) {
        if (orderAlreadyExists(order.getOrder_id())) {
            throw new OrderAlreadyExistsException("Order with id " + order.getOrder_id() + " already exists");
        }
        return orderRepository.save(order);
    }

    private boolean orderAlreadyExists(Long id) {
        return orderRepository.findById(id).isPresent();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (!order.isPresent()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        return order.get();
    }

    @Override
    public Order updateOrder(Order order, Long id) {
        if (!orderAlreadyExists(id)) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        order.setOrder_id(id);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderAlreadyExists(id)) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> getOrdersByUserId(Long user_id) {
        return orderRepository.findOrdersByUserId(user_id);
    }
}
