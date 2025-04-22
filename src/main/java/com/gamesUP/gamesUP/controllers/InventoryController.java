package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Inventory;
import com.gamesUP.gamesUP.services.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<Inventory> getAll() {
        return inventoryService.getAll();
    }

    @GetMapping("/{id}") public Inventory getById(@PathVariable Integer id) {
        return inventoryService.getById(id);
    }

    @PostMapping
    public Inventory create(@RequestBody Inventory inventory) {
        return inventoryService.save(inventory);
    }

    @PutMapping("/{id}")
    public Inventory update(@PathVariable Integer id, @RequestBody Inventory inventory) {
        return inventoryService.update(id, inventory);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        inventoryService.delete(id);
    }
}
