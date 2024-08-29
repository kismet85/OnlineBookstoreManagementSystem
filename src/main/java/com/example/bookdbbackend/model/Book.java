package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private Long id;
    private String title;
    private String isbn;
    private String genre;
    private String type;
    private int publication_year;
    private BigDecimal price;
    private String book_condition;
    private boolean reserved;
    private int inventory_id;
    private int publisher_id;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
