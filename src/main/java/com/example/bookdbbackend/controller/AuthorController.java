package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.exception.AuthorNotFoundException;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.service.IAuthorService;
import com.example.bookdbbackend.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173"})
public class AuthorController {
    private final IAuthorService iAuthorService;
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(iAuthorService.getAllAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        try {
            Author author = iAuthorService.getAuthorById(id);
            return new ResponseEntity<>(author, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Book>> getBooksByAuthorId(@PathVariable Long id) {
        try {
            List<Book> books = iAuthorService.getBooksByAuthorId(id);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{firstName}/{lastName}/books")
    public ResponseEntity<List<Book>> getBooksByAuthorFullName(@PathVariable String firstName, @PathVariable String lastName) {
        try {
            List<Book> books = iAuthorService.getBooksByAuthorName(firstName, lastName);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}