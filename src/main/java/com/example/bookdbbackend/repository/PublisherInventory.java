package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookdbbackend.model.Publisher;

public interface PublisherInventory extends JpaRepository<Publisher, Long>  {

}
