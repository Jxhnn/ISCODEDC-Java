package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Purchase;
import com.gamesUP.gamesUP.services.PurchaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<Purchase> getAll() {
        return purchaseService.getAll();
    }

    @GetMapping("/{id}") public Purchase getById(@PathVariable Integer id) {
        return purchaseService.getById(id);
    }

    @PostMapping
    public Purchase create(@RequestBody Purchase purchase) {
        return purchaseService.save(purchase);
    }

    @PutMapping("/{id}")
    public Purchase update(@PathVariable Integer id, @RequestBody Purchase purchase) {
        return purchaseService.update(id, purchase);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        purchaseService.delete(id);
    }
}
