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

/**
 * Service class for handling author-related operations.
 */
@Service
public class AuthorService implements IAuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Retrieves all authors.
     *
     * @return a list of all authors
     */
    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Retrieves an author by their ID.
     *
     * @param id the ID of the author
     * @return the author with the specified ID
     * @throws UserNotFoundException if the author is not found
     */
    @Override
    public Author getAuthorById(Long id) {
        if (!authorRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("Author not found with id: " + id);
        }
        return authorRepository.findById(id).get();
    }
}