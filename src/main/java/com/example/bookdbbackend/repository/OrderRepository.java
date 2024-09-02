package com.example.bookdbbackend.repository;


import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.user_id = :user_id")
    List<Order> findOrdersByUserId(Long user_id);
}
