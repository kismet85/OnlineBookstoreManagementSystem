package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "written_by")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WrittenBy {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}