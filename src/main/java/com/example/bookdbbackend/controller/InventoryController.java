package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.service.IInventoryService;
import com.example.bookdbbackend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Controller for handling inventory-related requests.
 */
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {

    private final IInventoryService iInventoryService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    /**
     * Endpoint for getting inventory by ID.
     *
     * @param id the ID of the inventory
     * @return the inventory with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        try {
            Inventory inventory = iInventoryService.getInventory(id);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for updating inventory by ID.
     *
     * @param updates the updates to apply to the inventory
     * @param id the ID of the inventory
     * @param token the JWT token from the Authorization header
     * @return the updated inventory
     */
    @PostMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@RequestBody Map<String, Object> updates, @PathVariable Long id, @RequestHeader("Authorization") String token) {
        String actualToken = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(actualToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(actualToken, userDetails)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Inventory updatedInventory = iInventoryService.updateInventory(updates, id);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}