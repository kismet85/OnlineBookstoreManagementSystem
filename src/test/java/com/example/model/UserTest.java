package com.example.model;

import com.example.bookdbbackend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGettersAndSetters() {
        user.setUser_id(1L);
        assertEquals(1L, user.getUser_id());
        user.setFirst_name("John");
        assertEquals("John", user.getFirst_name());
        user.setLast_name("Doe");
        assertEquals("Doe", user.getLast_name());
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
        user.setStreet_number(123);
        assertEquals(Integer.valueOf(123), user.getStreet_number());
        user.setStreet_name("Main Street");
        assertEquals("Main Street", user.getStreet_name());
        user.setPhone_number("555-1234");
        assertEquals("555-1234", user.getPhone_number());
        user.setPostal_code(12345);
        assertEquals(Integer.valueOf(12345), user.getPostal_code());
        user.setProvince("CA");
        assertEquals("CA", user.getProvince());
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities, "Authorities should not be null");
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));

        user.setRole("ADMIN");
        authorities = user.getAuthorities();
        assertNotNull(authorities, "Authorities should not be null");
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetUsername() {
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());
    }
}
