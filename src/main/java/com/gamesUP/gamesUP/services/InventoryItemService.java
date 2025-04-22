package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.InventoryItem;

import java.util.List;

public interface InventoryItemService {
    List<InventoryItem> getAll();
    InventoryItem getById(Integer id);
    InventoryItem save(InventoryItem inventoryItem);
    InventoryItem update(Integer id, InventoryItem inventoryItem);
    void delete(Integer id);
}
