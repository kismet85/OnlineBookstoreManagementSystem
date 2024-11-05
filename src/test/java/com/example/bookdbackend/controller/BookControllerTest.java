package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.BookController;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private IBookService iBookService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetBooks() {
//        when(iBookService.getAllBooks()).thenReturn(Collections.singletonList(new Book()));
//
//        ResponseEntity<List<Book>> response = bookController.getBooks();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(iBookService, times(1)).getAllBooks();
//    }

    @Test
    void testGetBookById() {
        Long id = 1L;
        Book book = new Book();
        when(iBookService.getBookById(id)).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBookById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getBookById(id);
    }

    @Test
    void testSearchBooks() {
        String query = "test";
        when(iBookService.searchBooks(query)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = bookController.searchBooks(query);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).searchBooks(query);
    }

    @Test
    void testGetBooksByTitle() {
        String title = "test";
        when(iBookService.getBooksByTitle(title)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = bookController.getBooksByTitle(title);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getBooksByTitle(title);
    }

    @Test
    void testGetBookByIsbn() {
        String isbn = "1234567890";
        Book book = new Book();
        when(iBookService.getBookByIsbn(isbn)).thenReturn(book);

        ResponseEntity<Book> response = bookController.getBookByIsbn(isbn);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getBookByIsbn(isbn);
    }

    @Test
    void testGetBooksByGenre() {
        String genre = "test";
        when(iBookService.getBooksByGenre(genre)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = bookController.getBooksByGenre(genre);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getBooksByGenre(genre);
    }

    @Test
    void testGetBooksByPublisher() {
        String publisher = "test";
        when(iBookService.getBooksByPublisherName(publisher)).thenReturn(Collections.singletonList(new Book()));

        ResponseEntity<List<Book>> response = bookController.getBooksByPublisher(publisher);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getBooksByPublisherName(publisher);
    }
}