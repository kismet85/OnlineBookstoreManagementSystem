package com.example.bookdbackend.controller;

import com.example.bookdbbackend.controller.InventoryController;
import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.service.IInventoryService;
import com.example.bookdbbackend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class InventoryControllerTest {

    @Mock
    private IInventoryService iInventoryService;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private InventoryController inventoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInventoryById() {
        Long id = 1L;
        Inventory inventory = new Inventory();
        when(iInventoryService.getInventory(id)).thenReturn(inventory);

        ResponseEntity<Inventory> response = inventoryController.getInventoryById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iInventoryService, times(1)).getInventory(id);
    }

    @Test
    void testUpdateInventory() {
        Long id = 1L;
        Map<String, Object> updates = Map.of("stock_level_new", 10);
        String token = "Bearer token";
        String actualToken = token.replace("Bearer ", "");
        String username = "admin";
        UserDetails userDetails = mock(UserDetails.class);
        Inventory updatedInventory = new Inventory();

        when(jwtService.extractUsername(actualToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(actualToken, userDetails)).thenReturn(true);

        when(iInventoryService.updateInventory(updates, id)).thenReturn(updatedInventory);

        ResponseEntity<Inventory> response = inventoryController.updateInventory(updates, id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iInventoryService, times(1)).updateInventory(updates, id);
    }
}