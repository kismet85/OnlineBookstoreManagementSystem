package com.example.bookdbbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bookdbbackend.model.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long>  {
    List<Publisher> findPublisherByName(String name);
    List<Publisher> findPublisherByCountry(String country);
}
