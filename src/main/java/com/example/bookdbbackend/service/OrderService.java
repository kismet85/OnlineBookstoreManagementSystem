package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.OrderItemResponseDto;
import com.example.bookdbbackend.dtos.OrderResponseDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    @Autowired
    private final OrderRepository orderRepository;
    @Override
    public Order addOrder(Order order) {
        if (order.getOrder_id() != null && orderAlreadyExists(order.getOrder_id())) {
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
