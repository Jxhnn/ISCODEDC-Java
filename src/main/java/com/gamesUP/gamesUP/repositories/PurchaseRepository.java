package com.gamesUP.gamesUP.repositories;

import com.gamesUP.gamesUP.model.Purchase;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
    List<Purchase> findAllByUserId(Integer id);
}
