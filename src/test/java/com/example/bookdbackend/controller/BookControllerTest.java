package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.BookController;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.InvalidDataException;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.HashMap;
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

    @Test
    void testGetBooks() {
        List<Book> books = Collections.singletonList(new Book());
        when(iBookService.getAllBooks(anyString())).thenReturn((List) books);

        ResponseEntity<List<?>> response = bookController.getBooks("en");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookService, times(1)).getAllBooks("en");
    }

    @Test
    void testGetBooks_NotFound() {
        when(iBookService.getAllBooks(anyString())).thenThrow(new RuntimeException());

        ResponseEntity<List<?>> response = bookController.getBooks("en");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

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
    void testGetBookById_NotFound() {
        Long id = 1L;
        when(iBookService.getBookById(id)).thenThrow(new RuntimeException());

        ResponseEntity<Book> response = bookController.getBookById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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

    @Test
    void testAddBook() {
        BookRequest bookRequest = new BookRequest();
        Book book = new Book();
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.createBook(any(BookRequest.class))).thenReturn(book);

        ResponseEntity<?> response = bookController.addBook(bookRequest, token);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(iBookService, times(1)).createBook(bookRequest);
    }

    @Test
    void testAddBook_Unauthorized() {
        BookRequest bookRequest = new BookRequest();
        String token = "Bearer invalidToken";
        String actualToken = "invalidToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<?> response = bookController.addBook(bookRequest, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(iBookService, never()).createBook(any(BookRequest.class));
    }

    @Test
    void testAddBook_Forbidden() {
        BookRequest bookRequest = new BookRequest();
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "regularUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        ResponseEntity<?> response = bookController.addBook(bookRequest, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody());
        verify(iBookService, never()).createBook(any(BookRequest.class));
    }

    @Test
    void testAddBook_InternalServerError() {
        BookRequest bookRequest = new BookRequest();
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.createBook(any(BookRequest.class))).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = bookController.addBook(bookRequest, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while adding the book: Error", response.getBody());
        verify(iBookService, times(1)).createBook(any(BookRequest.class));
    }

    @Test
    void testDeleteBook() {
        Long id = 1L;
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        ResponseEntity<String> response = bookController.deleteBook(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted book with id: " + id, response.getBody());
        verify(iBookService, times(1)).deleteBook(id);
    }

    @Test
    void testDeleteBook_Unauthorized() {
        Long id = 1L;
        String token = "Bearer invalidToken";
        String actualToken = "invalidToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<String> response = bookController.deleteBook(id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(iBookService, never()).deleteBook(anyLong());
    }

    @Test
    void testDeleteBook_Forbidden() {
        Long id = 1L;
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "regularUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        ResponseEntity<String> response = bookController.deleteBook(id, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied", response.getBody());
        verify(iBookService, never()).deleteBook(anyLong());
    }

    @Test
    void testDeleteBook_NotFound() {
        Long id = 1L;
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        doThrow(new EntityNotFoundException()).when(iBookService).deleteBook(id);

        ResponseEntity<String> response = bookController.deleteBook(id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
        verify(iBookService, times(1)).deleteBook(id);
    }

    @Test
    void testDeleteBook_InternalServerError() {
        Long id = 1L;
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        doThrow(new RuntimeException("Error")).when(iBookService).deleteBook(id);

        ResponseEntity<String> response = bookController.deleteBook(id, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred while deleting the book", response.getBody());
        verify(iBookService, times(1)).deleteBook(id);
    }

    @Test
    void testUpdateBook() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");

        Book updatedBook = new Book();
        updatedBook.setBook_id(id);
        updatedBook.setTitle("Updated Title");

        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.updateBook(updates, id)).thenReturn(updatedBook);

        ResponseEntity<?> response = bookController.updateBook(updates, id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedBook, response.getBody());
        verify(iBookService, times(1)).updateBook(updates, id);
    }

    @Test
    void testUpdateBook_NotFound() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");

        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.updateBook(updates, id)).thenThrow(new BookNotFoundException("Book not found"));

        ResponseEntity<?> response = bookController.updateBook(updates, id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book with ID " + id + " not found", response.getBody());
        verify(iBookService, times(1)).updateBook(updates, id);
    }

    @Test
    void testUpdateBook_InvalidData() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");

        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.updateBook(updates, id)).thenThrow(new InvalidDataException("Invalid data"));

        ResponseEntity<?> response = bookController.updateBook(updates, id, token);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data: Invalid data", response.getBody());
        verify(iBookService, times(1)).updateBook(updates, id);
    }

    @Test
    void testUpdateBook_Unauthorized() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");

        String token = "Bearer invalidToken";
        String actualToken = "invalidToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(false);

        ResponseEntity<?> response = bookController.updateBook(updates, id, token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
        verify(iBookService, never()).updateBook(any(), anyLong());
    }

    @Test
    void testUpdateBook_Forbidden() {
        Long id = 1L;
        Map<String, Object> updates = new HashMap<>();
        updates.put("title", "Updated Title");

        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "regularUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        ResponseEntity<?> response = bookController.updateBook(updates, id, token);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access denied: Admin privileges required", response.getBody());
        verify(iBookService, never()).updateBook(any(), anyLong());
    }

    @Test
    void testCreateDummyBook() {
        BookRequest bookRequest = new BookRequest();
        String token = "Bearer validToken";
        String actualToken = "validToken";
        String username = "adminUser";

        UserDetails userDetails = new User(username, "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);
        when(iBookService.createDummyBook()).thenReturn(bookRequest);

        ResponseEntity<?> response = bookController.createDummyBook(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookRequest, response.getBody());
        verify(iBookService, times(1)).createDummyBook();
    }
}