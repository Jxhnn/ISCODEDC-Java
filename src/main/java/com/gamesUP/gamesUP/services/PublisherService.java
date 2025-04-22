package com.gamesUP.gamesUP.services;

import com.gamesUP.gamesUP.model.Publisher;
import java.util.List;

public interface PublisherService {
    List<Publisher> getAll();
    Publisher getById(Integer id);
    Publisher save(Publisher publisher);
    Publisher update(Integer id, Publisher publisher);
    void delete(Integer id);
}
