package com.example.bookdbbackend.controller;

import com.example.bookdbbackend.model.Inventory;
import com.example.bookdbbackend.service.IBookService;
import com.example.bookdbbackend.service.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IInventoryService iInventoryService;

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        try {
            Inventory inventory = iInventoryService.getInventory(id);
            return new ResponseEntity<>(inventory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory, @PathVariable Long id) {
        try {
            Inventory updatedInventory = iInventoryService.updateInventory(inventory, id);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
