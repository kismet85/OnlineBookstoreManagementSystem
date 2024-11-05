package com.example.bookdbbackend.repository;

import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.BookJp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookJpRepository extends JpaRepository<BookJp, Long> {
}
