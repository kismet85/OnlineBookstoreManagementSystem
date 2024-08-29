package com.example.bookdbbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
        @Id
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String country;
        private int streetNumber;
        private String streetName;
        private int phoneNumber;
        private int postalCode;
        private String provice;
        private String role;

}
