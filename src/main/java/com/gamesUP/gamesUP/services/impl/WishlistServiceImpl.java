package com.gamesUP.gamesUP.services.impl;

import com.gamesUP.gamesUP.model.Wishlist;
import com.gamesUP.gamesUP.repositories.WishlistRepository;
import com.gamesUP.gamesUP.services.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    public WishlistServiceImpl(WishlistRepository wishlistRepository) { this.wishlistRepository = wishlistRepository; }

    public List<Wishlist> getAll() {
        return (List<Wishlist>) wishlistRepository.findAll();
    }

    public Wishlist getById(Integer id) {
        return wishlistRepository.findById(id).orElseThrow();
    }

    public Wishlist save(Wishlist user) {
        return wishlistRepository.save(user);
    }

    public Wishlist update(Integer id, Wishlist wishlist) {
        Wishlist existing = getById(id);
        existing.setGames(wishlist.getGames());
        existing.setUser(wishlist.getUser());
        return wishlistRepository.save(existing);
    }

    public void delete(Integer id) {
        wishlistRepository.deleteById(id);
    }
}

