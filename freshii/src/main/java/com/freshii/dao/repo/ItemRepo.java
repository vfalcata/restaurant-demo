package com.freshii.dao.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.freshii.model.Customer;
import com.freshii.model.Item;

public interface ItemRepo extends MongoRepository<Item, String>{
    @Query("{category:'?0'}")
    Item findItemByCategory(String category);
    
    @Query("{name:'?0'}")
    Item findItemByName(String name);
}
