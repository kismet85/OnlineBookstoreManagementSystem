package com.example.bookdbbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Entity class representing a user.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

        /**
         * The ID of the user.
         */
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long user_id;

        /**
         * The first name of the user.
         */
        private String first_name;

        /**
         * The last name of the user.
         */
        private String last_name;

        /**
         * The email of the user.
         */
        private String email;

        /**
         * The street number of the user's address.
         */
        private Integer street_number;

        /**
         * The street name of the user's address.
         */
        private String street_name;

        /**
         * The phone number of the user.
         */
        private String phone_number;

        /**
         * The postal code of the user's address.
         */
        private Integer postal_code;

        /**
         * The province of the user's address.
         */
        private String province;

        /**
         * The password of the user.
         */
        private String password;

        /**
         * The role of the user.
         */
        @Column(nullable = false)
        private String role = "USER";

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.role));
        }

        @Override
        public String getUsername() {
                return email;
        }

        @Override
        public boolean isAccountNonExpired() {
                return UserDetails.super.isAccountNonExpired();
        }

        @Override
        public boolean isAccountNonLocked() {
                return UserDetails.super.isAccountNonLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
                return UserDetails.super.isCredentialsNonExpired();
        }

        @Override
        public boolean isEnabled() {
                return UserDetails.super.isEnabled();
        }
}