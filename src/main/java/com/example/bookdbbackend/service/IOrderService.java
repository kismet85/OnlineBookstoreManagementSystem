package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.OrderResponseDto;
import com.example.bookdbbackend.model.Order;

import java.util.List;

/**
 * Service interface for handling order-related operations.
 */
public interface IOrderService {

    /**
     * Adds a new order.
     *
     * @param order the order to add
     * @return the added order
     */
    Order addOrder(Order order);

    /**
     * Retrieves all orders.
     *
     * @return a list of all orders
     */
    List<Order> getAllOrders();

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order
     * @return the order with the specified ID
     */
    OrderResponseDto getOrderById(Long id);

    /**
     * Updates an order with the specified updates.
     *
     * @param order the order with updates
     * @param id the ID of the order to update
     * @return the updated order
     */
    Order updateOrder(Order order, Long id);

    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     */
    void deleteOrder(Long id);

    /**
     * Retrieves orders by the user ID.
     *
     * @param user_id the ID of the user
     * @return a list of orders by the specified user
     */
    List<Order> getOrdersByUserId(Long user_id);
}