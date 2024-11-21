package com.example.bookdbbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity class representing an author.
 */
@Entity
@Table(name = "authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    /**
     * The ID of the author.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long author_id;

    /**
     * The first name of the author.
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * The last name of the author.
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * The set of books written by the author.
     */
    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Book> books = new HashSet<>();
}