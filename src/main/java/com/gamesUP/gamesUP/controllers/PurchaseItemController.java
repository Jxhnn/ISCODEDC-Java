package com.gamesUP.gamesUP.controllers;


import com.gamesUP.gamesUP.model.PurchaseItem;
import com.gamesUP.gamesUP.services.PurchaseItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase_items")
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    public PurchaseItemController(PurchaseItemService purchaseItemService) {
        this.purchaseItemService = purchaseItemService;
    }

    @GetMapping
    public List<PurchaseItem> getAll() {
        return purchaseItemService.getAll();
    }

    @GetMapping("/{id}") public PurchaseItem getById(@PathVariable Integer id) {
        return purchaseItemService.getById(id);
    }

    @PostMapping
    public PurchaseItem create(@RequestBody PurchaseItem purchaseItem) {
        return purchaseItemService.save(purchaseItem);
    }

    @PutMapping("/{id}")
    public PurchaseItem update(@PathVariable Integer id, @RequestBody PurchaseItem purchaseItem) {
        return purchaseItemService.update(id, purchaseItem);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        purchaseItemService.delete(id);
    }
}
