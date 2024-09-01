package com.example.bookdbbackend.service;

import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;

import java.util.List;

public interface IAuthorService {
    List<Author> getAllAuthors();
    Author getAuthorById(Long id);
    List<Book> getBooksByAuthorId(Long id);
}
