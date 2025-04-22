package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.PurchaseItem;
import java.util.List;

public interface PurchaseItemService {
    List<PurchaseItem> getAll();
    PurchaseItem getById(Integer id);
    PurchaseItem save(PurchaseItem purchaseItem);
    PurchaseItem update(Integer id, PurchaseItem purchaseItem);
    void delete(Integer id);
}
