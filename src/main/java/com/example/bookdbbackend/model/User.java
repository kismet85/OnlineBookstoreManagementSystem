package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.math.BigInteger;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
        @Id
        private Long user_id;
        private String first_name;
        private String last_name;
        private String email;
        private int street_number;
        private String street_name;
        private long phone_number;
        private int postal_code;
        private String province;
        private String password;
        private String role;
}