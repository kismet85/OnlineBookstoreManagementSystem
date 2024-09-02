package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    private final IOrderService iOrderService;

    private Order mergeNewAndOrderForUpdate(Order existingOrder, Order newOrder) {
        // If the new order object has a value for a field, update the existing order object with that value
        existingOrder.setOrder_id(newOrder.getOrder_id() != null ? newOrder.getOrder_id() : existingOrder.getOrder_id());
        existingOrder.setOrderDate(newOrder.getOrderDate() != null ? newOrder.getOrderDate() : existingOrder.getOrderDate());
        existingOrder.setTotal(newOrder.getTotal() != null ? newOrder.getTotal() : existingOrder.getTotal());
        existingOrder.setUser(newOrder.getUser() != null ? newOrder.getUser() : existingOrder.getUser());
        return existingOrder;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        return new ResponseEntity<>(iOrderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            Order order = iOrderService.getOrderById(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return iOrderService.addOrder(order);
    }

    @PostMapping("/{id}")
    public Order updateOrder(@RequestBody Order order, @PathVariable Long id) {
        return iOrderService.updateOrder(order, id);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        iOrderService.deleteOrder(id);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long user_id) {
        List<Order> orders = iOrderService.getOrdersByUserId(user_id);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
