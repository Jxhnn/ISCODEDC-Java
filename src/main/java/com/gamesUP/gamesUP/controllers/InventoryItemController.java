package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.model.InventoryItem;
import com.gamesUP.gamesUP.services.InventoryItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory_items")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping
    public List<InventoryItem> getAll() {
        return inventoryItemService.getAll();
    }

    @GetMapping("/{id}")
    public InventoryItem getById(@PathVariable Integer id) {
        return inventoryItemService.getById(id);
    }

    @PostMapping
    public InventoryItem create(@RequestBody InventoryItem inventoryItem) {
        return inventoryItemService.save(inventoryItem);
    }

    @PutMapping("/{id}")
    public InventoryItem update(@PathVariable Integer id, @RequestBody InventoryItem inventoryItem) {
        return inventoryItemService.update(id, inventoryItem);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        inventoryItemService.delete(id);
    }
}
