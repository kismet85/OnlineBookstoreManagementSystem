package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.User;
import com.example.bookdbbackend.service.IBookService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.bookdbbackend.model.Book;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService iBookService;

    private Book mergeNewAndOldUserForUpdate(Book existingBook, Book newBook) {
        // If the new book object has a value for a field, update the existing book object with that value
        existingBook.setTitle(newBook.getTitle() != null ? newBook.getTitle() : existingBook.getTitle());
        existingBook.setIsbn(newBook.getIsbn() != null ? newBook.getIsbn() : existingBook.getIsbn());
        existingBook.setGenre(newBook.getGenre() != null ? newBook.getGenre() : existingBook.getGenre());
        existingBook.setType(newBook.getType() != null ? newBook.getType() : existingBook.getType());
        existingBook.setPublication_year(newBook.getPublication_year() != 0 ? newBook.getPublication_year() : existingBook.getPublication_year());
        existingBook.setPrice(newBook.getPrice() != null && !newBook.getPrice().equals(BigDecimal.ZERO) ? newBook.getPrice() : existingBook.getPrice());
        existingBook.setReserved(newBook.isReserved() && existingBook.isReserved());
        existingBook.setInventory(newBook.getInventory() != null ? newBook.getInventory() : existingBook.getInventory());
        existingBook.setPublisher(newBook.getPublisher() != null ? newBook.getPublisher() : existingBook.getPublisher());
        return existingBook;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(iBookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Book book = iBookService.getBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/author/{author_id}")
    public ResponseEntity<List<Book>> getBooksByAuthorId(@PathVariable Long author_id) {
        List<Book> books = iBookService.getBooksByAuthorId(author_id);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return iBookService.addBook(book);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody Book book, @PathVariable Long id) {
        try {
            Book existingBook = iBookService.getBookById(id);
            Book mergedBook = mergeNewAndOldUserForUpdate(existingBook, book);
            Book updatedBook = iBookService.updateBook(mergedBook, id);

            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}