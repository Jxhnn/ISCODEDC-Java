package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.InventoryItem;
import com.gamesUP.gamesUP.repositories.InventoryItemRepository;
import com.gamesUP.gamesUP.services.InventoryItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getAll() {
        return (List<InventoryItem>) inventoryItemRepository.findAll();
    }

    public InventoryItem getById(Integer id) {
        return inventoryItemRepository.findById(id).orElseThrow();
    }

    public InventoryItem save(InventoryItem review) {
        return inventoryItemRepository.save(review);
    }

    public InventoryItem update(Integer id, InventoryItem inventoryItem) {
        InventoryItem existing = getById(id);
        existing.setInventory(inventoryItem.getInventory());
        existing.setGame(inventoryItem.getGame());
        existing.setQuantity(inventoryItem.getQuantity());
        return inventoryItemRepository.save(existing);
    }

    public void delete(Integer id) {
        inventoryItemRepository.deleteById(id);
    }
}

