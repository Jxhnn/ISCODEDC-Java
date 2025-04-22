package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Inventory;
 import com.gamesUP.gamesUP.repositories.InventoryRepository;
import com.gamesUP.gamesUP.services.InventoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAll() {
        return (List<Inventory>) inventoryRepository.findAll();
    }

    public Inventory getById(Integer id) {
        return inventoryRepository.findById(id).orElseThrow();
    }

    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Inventory update(Integer id, Inventory inventory) {
        Inventory existing = getById(id);
        existing.setStock(inventory.getStock());
        return inventoryRepository.save(existing);
    }

    public void delete(Integer id) {
        inventoryRepository.deleteById(id);
    }
}

