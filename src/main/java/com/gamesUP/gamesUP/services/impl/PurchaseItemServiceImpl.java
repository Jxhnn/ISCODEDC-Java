package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.PurchaseItem;
import com.gamesUP.gamesUP.repositories.PurchaseItemRepository;
import com.gamesUP.gamesUP.services.PurchaseItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseItemServiceImpl implements PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    public PurchaseItemServiceImpl(PurchaseItemRepository purchaseItemRepository) {
        this.purchaseItemRepository = purchaseItemRepository;
    }

    public List<PurchaseItem> getAll() {
        return (List<PurchaseItem>) purchaseItemRepository.findAll();
    }

    public PurchaseItem getById(Integer id) {
        return purchaseItemRepository.findById(id).orElseThrow();
    }

    public PurchaseItem save(PurchaseItem purchaseItem) {
        return purchaseItemRepository.save(purchaseItem);
    }

    public PurchaseItem update(Integer id, PurchaseItem purchaseItem) {
        PurchaseItem existing = getById(id);
        existing.setGame(purchaseItem.getGame());
        existing.setPurchase(purchaseItem.getPurchase());
        existing.setPrice(purchaseItem.getPrice());
        existing.setQuantity(purchaseItem.getQuantity());
        return purchaseItemRepository.save(existing);
    }

    public void delete(Integer id) {
        purchaseItemRepository.deleteById(id);
    }
}

