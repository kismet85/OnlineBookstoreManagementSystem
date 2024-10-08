package com.example.bookdbbackend.service;

import com.example.bookdbbackend.exception.AuthorNotFoundException;
import com.example.bookdbbackend.exception.UserNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.repository.AuthorRepository;
import com.example.bookdbbackend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorService implements IAuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        if (!authorRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("Author not found with id: " + id);
        }
        return authorRepository.findById(id).get();
    }
}