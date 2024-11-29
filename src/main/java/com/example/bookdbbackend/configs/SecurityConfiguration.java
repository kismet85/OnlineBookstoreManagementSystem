package com.example.bookdbbackend.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class for setting up HTTP security and CORS.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Constructor for SecurityConfiguration.
     *
     * @param authenticationProvider  the authentication provider
     * @param jwtAuthenticationFilter the JWT authentication filter
     */
    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())  // Apply CORS settings
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**",
                                        "/api/v1/auth/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html/**",
                                        "/swagger-resources",
                                        "/swagger-resources/**",
                                        "/swagger-resources/**",
                                        "/configuration/ui",
                                        "/configuration/security",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors").permitAll()
                                .requestMatchers(HttpMethod.POST, "/orders/addOrder").permitAll()
                                .requestMatchers(HttpMethod.GET, "/orders/me").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/search").permitAll()
                                .requestMatchers(HttpMethod.GET, "/users/me").permitAll()
                                .requestMatchers(HttpMethod.POST, "/users/update/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/title/{title}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/genre/{genre}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/publisher/{publisher}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/isbn/{isbn}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/books/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/authors/{id}/books").permitAll()
                                .requestMatchers(HttpMethod.GET, "/inventory/{id}").permitAll()
                                .requestMatchers(HttpMethod.POST, "/inventory/{id}").permitAll()
                                .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form.disable())
                .httpBasic(AbstractHttpConfigurer::disable);
        return http.build();
    }

    /**
     * Configures CORS settings.
     *
     * @return the configured CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5174", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept-Language"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}