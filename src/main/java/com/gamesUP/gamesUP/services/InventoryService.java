package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getAll();
    Inventory getById(Integer id);
    Inventory save(Inventory inventory);
    Inventory update(Integer id, Inventory inventory);
    void delete(Integer id);
}
