package com.example.bookdbbackend.repository;


import com.example.bookdbbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
