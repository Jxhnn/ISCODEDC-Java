package com.gamesUP.gamesUP.controllers;

import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.services.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public List<Wishlist> getAll() {
        return wishlistService.getAll();
    }

    @GetMapping("/{id}") public Wishlist getById(@PathVariable Integer id) {
        return wishlistService.getById(id);
    }

    @PostMapping
    public Wishlist create(@RequestBody Wishlist wishlist) {
        return wishlistService.save(wishlist);
    }

    @PutMapping("/{id}")
    public Wishlist update(@PathVariable Integer id, @RequestBody Wishlist user) {
        return wishlistService.update(id, user);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Integer id) {
        wishlistService.delete(id);
    }
}
