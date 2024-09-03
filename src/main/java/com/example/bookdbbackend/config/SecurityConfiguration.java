package com.example.bookdbbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/me").hasRole("USER")
                                .requestMatchers(HttpMethod.GET, "/books/title/{title}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/genre/{genre}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/publisher/{publisher}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/isbn/{isbn}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/author/{author_id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}/books").permitAll()
                                .anyRequest().hasRole("ADMIN")
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}