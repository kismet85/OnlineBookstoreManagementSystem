package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.exception.BookNotFoundException;
import com.example.bookdbbackend.exception.InvalidDataException;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.bookdbbackend.model.Book;

import java.util.List;
import java.util.Map;

/**
 * Controller for handling book-related requests.
 */
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService iBookService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    /**
     * Endpoint for getting all books.
     *
     * @param language the language for the response
     * @return a list of all books
     */
    @GetMapping
    public ResponseEntity<List<?>> getBooks(@RequestHeader("Accept-Language") String language) {
        try {
            List<?> books = iBookService.getAllBooks(language);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting a book by ID.
     *
     * @param id the ID of the book
     * @return the book with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Book book = iBookService.getBookById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for searching books by a query.
     *
     * @param query the search query
     * @return a list of books matching the query
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        List<Book> books = iBookService.searchBooks(query);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Endpoint for adding a new book.
     *
     * @param bookRequest the book request data
     * @param token the JWT token from the Authorization header
     * @return the created book
     */
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookRequest bookRequest, @RequestHeader("Authorization") String token) {
        try {
            // Extract and validate the token
            String actualToken = token.replace("Bearer ", "");
            String username = jwtService.extractUsername(actualToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtService.isTokenValid(actualToken, userDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            // Check if the user has admin privileges
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            // Create the book and return the response
            Book book = iBookService.createBook(bookRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);

        } catch (RuntimeException e) {
            // Handle specific exceptions or log the error if needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the book: " + e.getMessage());
        }
    }

    /**
     * Endpoint for deleting a book by ID.
     *
     * @param id the ID of the book
     * @param token the JWT token from the Authorization header
     * @return a response indicating the result of the deletion
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Check if the token is valid
        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Check if the user is an admin
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }

        try {
            iBookService.deleteBook(id);
            return new ResponseEntity<>("Successfully deleted book with id: " + id, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("An error occurred while deleting the book", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for creating a dummy book.
     *
     * @param token the JWT token from the Authorization header
     * @return the created dummy book request
     */
    @GetMapping("/dummy")
    public ResponseEntity<?> createDummyBook(@RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Check if the token is valid
        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // Check if the user is an admin
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }

        BookRequest bookRequest = iBookService.createDummyBook();
        return new ResponseEntity<>(bookRequest, HttpStatus.OK);
    }

    /**
     * Endpoint for updating a book by ID.
     *
     * @param updates the updates to apply to the book
     * @param id the ID of the book
     * @param token the JWT token from the Authorization header
     * @return the updated book
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> updateBook(@RequestBody Map<String, Object> updates, @PathVariable Long id, @RequestHeader("Authorization") String token) {

        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);

        // Load user details using the extracted username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Check if token is valid
        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // Check if the user has admin privileges
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        logger.info("User role is admin: " + isAdmin);

        // If the user is not an admin, return a 403 Forbidden status
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: Admin privileges required");
        }

        try {
            // Attempt to update the book
            Book updatedBook = iBookService.updateBook(updates, id);

            // Return the updated book and a 200 OK status
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);

        } catch (BookNotFoundException e) {
            // Return 404 Not Found if the book doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID " + id + " not found");

        } catch (InvalidDataException e) {
            // Return 400 Bad Request if the request data is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());

        } catch (Exception e) {
            // Log the exception and return 500 Internal Server Error for other errors
            logger.error("Error updating book with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Endpoint for getting books by title.
     *
     * @param title the title of the books
     * @return a list of books with the specified title
     */
    @GetMapping("/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        try {
            List<Book> books = iBookService.getBooksByTitle(title);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting a book by ISBN.
     *
     * @param isbn the ISBN of the book
     * @return the book with the specified ISBN
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
        try {
            Book book = iBookService.getBookByIsbn(isbn);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting books by genre.
     *
     * @param genre the genre of the books
     * @return a list of books with the specified genre
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Book>> getBooksByGenre(@PathVariable String genre) {
        try {
            List<Book> books = iBookService.getBooksByGenre(genre);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for getting books by publisher.
     *
     * @param publisher the publisher of the books
     * @return a list of books with the specified publisher
     */
    @GetMapping("/publisher/{publisher}")
    public ResponseEntity<List<Book>> getBooksByPublisher(@PathVariable String publisher) {
        try {
            List<Book> books = iBookService.getBooksByPublisherName(publisher);
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}