package com.example.bookdbbackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryRequest {
    private int stockLevelUsed;
    private int stockLevelNew;
    private int reservedStock;
}
