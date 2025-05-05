package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Wishlist;

import java.util.List;

public interface WishlistService {
    List<Wishlist> getAll();
    Wishlist getById(Integer id);
    List<Wishlist> getByUserId(Integer id);
    Wishlist save(Wishlist wishlist);
    Wishlist update(Integer id, Wishlist wishlist);
    void delete(Integer id);
}
