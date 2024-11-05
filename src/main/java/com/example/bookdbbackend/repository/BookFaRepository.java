package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.BookFa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookFaRepository extends JpaRepository<BookFa, Long> {
}
