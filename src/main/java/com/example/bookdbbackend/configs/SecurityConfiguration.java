package com.example.bookdbbackend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .cors(withDefaults())  // Apply CORS settings
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors").permitAll()
                                .requestMatchers(HttpMethod.POST, "/orders/addOrder").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/search/{searchTerm}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/me").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/title/{title}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/genre/{genre}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/publisher/{publisher}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/isbn/{isbn}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}/books").permitAll()
                                .anyRequest().hasRole("ADMIN")
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}