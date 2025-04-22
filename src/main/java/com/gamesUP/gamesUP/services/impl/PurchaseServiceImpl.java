package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.repositories.PurchaseRepository;
import com.gamesUP.gamesUP.services.PurchaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> getAll() {
        return (List<Purchase>) purchaseRepository.findAll();
    }

    public Purchase getById(Integer id) {
        return purchaseRepository.findById(id).orElseThrow();
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase update(Integer id, Purchase purchase) {
        Purchase existing = getById(id);
        existing.setUser(purchase.getUser());
        existing.setDate(purchase.getDate());
        existing.setPaid(purchase.isPaid());
        existing.setDelivered(purchase.isDelivered());
        existing.setArchived(purchase.isArchived());
        existing.setPurchaseLines(purchase.getPurchaseLines());
        return purchaseRepository.save(existing);
    }

    public void delete(Integer id) {
        purchaseRepository.deleteById(id);
    }
}

