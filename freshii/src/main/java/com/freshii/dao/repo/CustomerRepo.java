package com.freshii.dao.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.freshii.model.Customer;


public interface CustomerRepo extends MongoRepository<Customer, String>{
    @Query("{username:'?0',password:'?1'}")
    Customer findCustomerWithUsernameAndPassword(String username, String password);
	
}
