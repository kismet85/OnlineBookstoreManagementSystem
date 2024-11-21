package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing a book.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    /**
     * The ID of the book.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    /**
     * The title of the book.
     */
    private String title;

    /**
     * The ISBN of the book.
     */
    private String isbn;

    /**
     * The genre of the book.
     */
    private String genre;

    /**
     * The type of the book.
     */
    private String type;

    /**
     * The publication year of the book.
     */
    private int publication_year;

    /**
     * The price of the book.
     */
    private BigDecimal price;

    /**
     * The condition of the book.
     */
    private String book_condition;

    /**
     * Whether the book is reserved.
     */
    private boolean reserved;

    /**
     * The URL of the book's image.
     */
    private String image_url;

    /**
     * The inventory associated with the book.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    /**
     * The publisher of the book.
     */
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    /**
     * The set of authors who wrote the book.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "written_by",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
}