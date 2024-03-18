package com.freshii.dao.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.freshii.model.Customer;
import com.freshii.model.Manager;

public interface ManagerRepo extends MongoRepository<Manager, String>{
    @Query("{username:'?0',password:'?1'}")
    Manager findManagerWithUsernameAndPassword(String username, String password);
}
