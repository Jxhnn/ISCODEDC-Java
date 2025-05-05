package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Wishlist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WishlistRepository extends CrudRepository<Wishlist, Integer> {
    List<Wishlist> findAllByUserId(Integer id);
}
