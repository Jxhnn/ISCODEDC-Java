package com.gamesUP.gamesUP.services;


import com.gamesUP.gamesUP.model.Purchase;

import java.util.List;

public interface PurchaseService {
    List<Purchase> getAll();
    Purchase getById(Integer id);
    Purchase save(Purchase purchase);
    Purchase update(Integer id, Purchase purchase);
    void delete(Integer id);
}
