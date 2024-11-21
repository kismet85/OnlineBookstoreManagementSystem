package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.AuthorNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.service.IAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling author-related requests.
 */
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthorController {
    private final IAuthorService iAuthorService;

    /**
     * Endpoint for getting all authors.
     *
     * @return a list of all authors
     */
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(iAuthorService.getAllAuthors(), HttpStatus.OK);
    }

    /**
     * Endpoint for getting an author by ID.
     *
     * @param id the ID of the author
     * @return the author with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        try {
            Author author = iAuthorService.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}