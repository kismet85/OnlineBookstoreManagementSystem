package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.OrderItemResponseDto;
import com.example.bookdbbackend.dtos.OrderResponseDto;
import com.example.bookdbbackend.exception.OrderAlreadyExistsException;
import com.example.bookdbbackend.exception.OrderNotFoundException;
import com.example.bookdbbackend.model.Order;
import com.example.bookdbbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling order-related operations.
 */
@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;

    /**
     * Adds a new order.
     *
     * @param order the order to add
     * @return the added order
     * @throws OrderAlreadyExistsException if the order already exists
     */
    @Override
    public Order addOrder(Order order) {
        if (order.getOrder_id() != null && orderAlreadyExists(order.getOrder_id())) {
            throw new OrderAlreadyExistsException("Order with id " + order.getOrder_id() + " already exists");
        }
        return orderRepository.save(order);
    }

    /**
     * Checks if an order already exists by its ID.
     *
     * @param id the ID of the order
     * @return true if the order exists, false otherwise
     */
    private boolean orderAlreadyExists(Long id) {
        return orderRepository.findById(id).isPresent();
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of all orders
     */
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param id the ID of the order
     * @return the order with the specified ID
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    public OrderResponseDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }

        Order foundOrder = order.get();
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setTotal(foundOrder.getTotal());
        responseDto.setOrderDate(foundOrder.getOrderDate());
        responseDto.setUserEmail(foundOrder.getUser().getEmail());

        List<OrderItemResponseDto> orderItemDtos = foundOrder.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemResponseDto itemDto = new OrderItemResponseDto();
                    itemDto.setBookTitle(orderItem.getBook().getTitle());
                    return itemDto;
                })
                .collect(Collectors.toList());

        responseDto.setOrderItems(orderItemDtos);
        return responseDto;
    }

    /**
     * Updates an order with the specified updates.
     *
     * @param order the order with updates
     * @param id the ID of the order to update
     * @return the updated order
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    public Order updateOrder(Order order, Long id) {
        if (!orderAlreadyExists(id)) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        order.setOrder_id(id);
        return orderRepository.save(order);
    }

    /**
     * Deletes an order by its ID.
     *
     * @param id the ID of the order to delete
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    public void deleteOrder(Long id) {
        if (!orderAlreadyExists(id)) {
            throw new OrderNotFoundException("Order with id " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    /**
     * Retrieves orders by the user ID.
     *
     * @param user_id the ID of the user
     * @return a list of orders by the specified user
     */
    @Override
    public List<Order> getOrdersByUserId(Long user_id) {
        return orderRepository.findOrdersByUserId(user_id);
    }
}