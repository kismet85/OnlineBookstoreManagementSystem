package com.example.bookdbbackend.service;

import com.example.bookdbbackend.dtos.AuthorRequest;
import com.example.bookdbbackend.dtos.BookRequest;
import com.example.bookdbbackend.dtos.InventoryRequest;
import com.example.bookdbbackend.dtos.PublisherRequest;
import com.example.bookdbbackend.exception.*;
import com.example.bookdbbackend.model.Author;
import com.example.bookdbbackend.model.Book;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.model.Publisher;
import com.example.bookdbbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

/**
 * Service class for handling book-related operations.
 */
@Service
@RequiredArgsConstructor
public class BookService implements IBookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private static final String NOT_FOUND = " not found";
    private static final String BOOK_ID = "Book with id ";

    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final BookJpRepository bookJpRepository;
    @Autowired
    private final BookFaRepository bookFaRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final PublisherRepository publisherRepository;
    @Autowired
    private final InventoryRepository inventoryRepository;

    /**
     * Creates a new book.
     *
     * @param bookRequest the book request details
     * @return the created book
     */
    @Override
    public Book createBook(BookRequest bookRequest) {
        Inventory inventory = new Inventory();
        inventory.setStock_level_used(bookRequest.getInventory().getStockLevelUsed());
        inventory.setStock_level_new(bookRequest.getInventory().getStockLevelNew());
        inventory.setReserved_stock(bookRequest.getInventory().getReservedStock());
        inventoryRepository.save(inventory);

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setIsbn(bookRequest.getIsbn());
        book.setGenre(bookRequest.getGenre());
        book.setType(bookRequest.getType());
        book.setPublication_year(bookRequest.getPublicationYear());
        book.setPrice(bookRequest.getPrice());
        book.setBook_condition(bookRequest.getBookCondition());
        book.setReserved(bookRequest.isReserved());
        book.setImage_url(bookRequest.getImageUrl());
        book.setInventory(inventory);

        // Set the publisher
        if (bookRequest.getPublisherId() != null) {
            // Existing publisher
            Publisher publisher = publisherRepository.findById(bookRequest.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        } else if (bookRequest.getPublisher() != null) {
            // New publisher
            Publisher publisher = new Publisher();
            publisher.setName(bookRequest.getPublisher().getName());
            publisher.setCountry(bookRequest.getPublisher().getCountry());
            publisher = publisherRepository.save(publisher);
            book.setPublisher(publisher);
        }

        Set<Author> authors = new HashSet<>();
        for (AuthorRequest authorRequest : bookRequest.getAuthors()) {
            Author author = new Author();
            author.setFirstName(authorRequest.getFirstName());
            author.setLastName(authorRequest.getLastName());

            // Check if the author already exists (to avoid duplicates)
            Optional<Author> existingAuthor = authorRepository
                    .findByFirstNameAndLastName(authorRequest.getFirstName(), authorRequest.getLastName());
            if (existingAuthor.isPresent()) {
                authors.add(existingAuthor.get());
            } else {
                authors.add(authorRepository.save(author));
            }
        }
        book.setAuthors(authors);

        return bookRepository.save(book);
    }

    /**
     * Checks if a book already exists by its ID.
     *
     * @param id the ID of the book
     * @return true if the book exists, false otherwise
     */
    private boolean bookAlreadyExists(Long id) {
        return bookRepository.findById(id).isPresent();
    }

    /**
     * Retrieves all books based on the specified language.
     *
     * @param language the language code
     * @return a list of books
     */
    @Override
    public List<?> getAllBooks(String language) {
        return switch (language) {
            case "ja" -> bookJpRepository.findAll();
            case "fa" -> bookFaRepository.findAll();
            default -> bookRepository.findAll();
        };
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book
     * @return the book with the specified ID
     * @throws BookNotFoundException if the book is not found
     */
    @Override
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new BookNotFoundException(BOOK_ID + id + NOT_FOUND);
        }

        return book.get();
    }

    /**
     * Updates a book with the specified updates.
     *
     * @param updates the updates to apply
     * @param id      the ID of the book to update
     * @return the updated book
     * @throws InvalidDataException if there is an error updating the book
     */
    @Override
    public Book updateBook(Map<String, Object> updates, Long id) {
        try {
            logger.info("Updates: " + updates.toString());
            Book book = getBookById(id);
            if (!bookAlreadyExists(id)) {
                throw new BookNotFoundException(BOOK_ID + id + NOT_FOUND);
            }

            Map<String, Consumer<Object>> updateActions = new HashMap<>();
            updateActions.put("title", value -> book.setTitle((String) value));
            updateActions.put("isbn", value -> book.setIsbn((String) value));
            updateActions.put("genre", value -> book.setGenre((String) value));
            updateActions.put("type", value -> book.setType((String) value));
            updateActions.put("price", value -> {
                if (value instanceof Number) {
                    book.setPrice(BigDecimal.valueOf(((Number) value).doubleValue()));
                }
            });
            updateActions.put("publication_year", value -> book.setPublication_year((Integer) value));
            updateActions.put("book_condition", value -> book.setBook_condition((String) value));
            updateActions.put("image_url", value -> book.setImage_url((String) value));
            updateActions.put("reserved", value -> book.setReserved((Boolean) value));
            updateActions.put("inventory", value -> updateInventory(book.getInventory(), (Map<String, Integer>) value));
            updateActions.put("publisher", value -> updatePublisher(book.getPublisher(), (Map<String, String>) value));
            updateActions.put("authors", value -> updateAuthors(book, (List<Map<String, String>>) value));

            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                updateActions.getOrDefault(entry.getKey(), key -> logger.warn("No matching case for key: " + key))
                        .accept(entry.getValue());
            }
            return bookRepository.save(book);
        } catch (Exception e) {
            logger.error("Error updating book with ID " + id, e);
            throw new InvalidDataException(e.getMessage());
        }
    }


    /**
     * Creates a dummy book request.
     *
     * @return the dummy book request
     */
    @Override
    public BookRequest createDummyBook() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("");
        bookRequest.setIsbn("");
        bookRequest.setGenre("");
        bookRequest.setType("");
        bookRequest.setPublicationYear(0);
        bookRequest.setPrice(BigDecimal.valueOf(0));
        bookRequest.setBookCondition("");
        bookRequest.setReserved(false);
        bookRequest.setImageUrl("");

        PublisherRequest publisherRequest = new PublisherRequest();
        publisherRequest.setCountry("");
        publisherRequest.setName("");
        bookRequest.setPublisher(publisherRequest);

        bookRequest.setAuthors(new ArrayList<>());

        InventoryRequest inventoryRequest = new InventoryRequest();
        inventoryRequest.setStockLevelUsed(0);
        inventoryRequest.setStockLevelNew(0);
        inventoryRequest.setReservedStock(0);
        bookRequest.setInventory(inventoryRequest);

        return bookRequest;
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @throws BookNotFoundException if the book is not found
     */
    @Override
    public void deleteBook(Long id) {
        if (!bookAlreadyExists(id)) {
            throw new BookNotFoundException(BOOK_ID + id + NOT_FOUND);
        }
        bookRepository.deleteById(id);
    }

    /**
     * Retrieves books by their title.
     *
     * @param title the title of the books
     * @return a list of books with the specified title
     * @throws BookNotFoundException if no books are found
     */
    @Override
    public List<Book> getBooksByTitle(String title) {
        List<Book> books = bookRepository.findBooksByTitleContainingIgnoreCase(title);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with title " + title + NOT_FOUND);
        }
        return books;
    }

    /**
     * Retrieves a book by its ISBN.
     *
     * @param isbn the ISBN of the book
     * @return the book with the specified ISBN
     * @throws BookNotFoundException if the book is not found
     */
    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> book = bookRepository.findBookByIsbn(isbn);
        if (!book.isPresent()) {
            throw new BookNotFoundException("Book with isbn " + isbn + NOT_FOUND);
        }
        return book.get();
    }

    /**
     * Retrieves books by their author's name.
     *
     * @param author the author's name
     * @return a list of books by the specified author
     * @throws BookNotFoundException if no books are found
     */
    @Override
    public List<Book> getBooksByAuthor(String author) {
        List<Book> books = bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(author);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with author " + author + NOT_FOUND);
        }
        return books;
    }

    /**
     * Retrieves books by their genre.
     *
     * @param genre the genre of the books
     * @return a list of books with the specified genre
     * @throws BookNotFoundException if no books are found
     */
    @Override
    public List<Book> getBooksByGenre(String genre) {
        List<Book> books = bookRepository.findBooksByGenreContainingIgnoreCase(genre);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with genre " + genre + NOT_FOUND);
        }
        return books;
    }

    /**
     * Retrieves books by their publisher's name.
     *
     * @param publisher the publisher's name
     * @return a list of books by the specified publisher
     * @throws BookNotFoundException if no books are found
     */
    @Override
    public List<Book> getBooksByPublisherName(String publisher) {
        List<Book> books = bookRepository.findBooksByPublisherName(publisher);
        if (books.isEmpty()) {
            throw new BookNotFoundException("Book with publisher " + publisher + NOT_FOUND);
        }
        return books;
    }

    /**
     * Searches for books based on a search term.
     *
     * @param searchTerm the search term
     * @return a list of books matching the search term
     * @throws IllegalArgumentException if the search term is less than 3 characters long
     */
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm.length() < 3) {
            throw new IllegalArgumentException("Search term must be at least 3 characters long");
        }

        List<Book> booksByTitle = bookRepository.findBooksByTitleContainingIgnoreCase(searchTerm);
        List<Book> booksByPublisher = bookRepository.findBooksByPublisherName(searchTerm);
        List<Book> booksByGenre = bookRepository.findBooksByGenreContainingIgnoreCase(searchTerm);
        List<Book> booksByIsbn = bookRepository.findBooksByIsbnContainingIgnoreCase(searchTerm);
        List<Book> booksByAuthor = bookRepository.findBooksByAuthorsFirstNameContainingIgnoreCase(searchTerm);
        List<Book> booksByAuthorLastName = bookRepository.findBooksByAuthorsLastNameContainingIgnoreCase(searchTerm);

        if (!booksByIsbn.isEmpty()) {
            return booksByIsbn;
        }

        List<Book> combinedResults = new ArrayList<>();
        combinedResults.addAll(booksByTitle);
        combinedResults.addAll(booksByPublisher);
        combinedResults.addAll(booksByGenre);
        combinedResults.addAll(booksByAuthor);
        combinedResults.addAll(booksByAuthorLastName);

        combinedResults = new ArrayList<>(new HashSet<>(combinedResults));

        return combinedResults;
    }

    /**
     * Updates the inventory with the specified updates.
     *
     * @param inventory       the inventory to update
     * @param inventoryUpdates the updates to apply
     */
    private void updateInventory(Inventory inventory, Map<String, Integer> inventoryUpdates) {
        if (inventoryUpdates.containsKey("stock_level_used")) {
            inventory.setStock_level_used(inventoryUpdates.get("stock_level_used"));
        }
        if (inventoryUpdates.containsKey("stock_level_new")) {
            inventory.setStock_level_new(inventoryUpdates.get("stock_level_new"));
        }
        if (inventoryUpdates.containsKey("reserved_stock")) {
            inventory.setReserved_stock(inventoryUpdates.get("reserved_stock"));
        }
        inventoryRepository.save(inventory);
    }

    /**
     * Updates the publisher with the specified updates.
     *
     * @param publisher        the publisher to update
     * @param publisherUpdates the updates to apply
     */
    private void updatePublisher(Publisher publisher, Map<String, String> publisherUpdates) {
        if (publisherUpdates.containsKey("name")) {
            publisher.setName(publisherUpdates.get("name"));
        }
        if (publisherUpdates.containsKey("country")) {
            publisher.setCountry(publisherUpdates.get("country"));
        }
        publisherRepository.save(publisher);
    }

    /**
     * Updates the authors with the specified updates.
     *
     * @param book          the book to update
     * @param authorUpdates the updates to apply
     */
    private void updateAuthors(Book book, List<Map<String, String>> authorUpdates) {
        Set<Author> authors = new HashSet<>();
        for (Map<String, String> authorUpdate : authorUpdates) {
            Long authorId = Long.valueOf(authorUpdate.get("author_id"));
            Optional<Author> existingAuthor = authorRepository.findById(authorId);
            if (existingAuthor.isPresent()) {
                Author author = existingAuthor.get();
                author.setFirstName(authorUpdate.get("firstName"));
                author.setLastName(authorUpdate.get("lastName"));
                authors.add(authorRepository.save(author));
            } else {
                logger.warn("Author with ID " + authorId + NOT_FOUND);
            }
        }
        book.setAuthors(authors);
    }
}