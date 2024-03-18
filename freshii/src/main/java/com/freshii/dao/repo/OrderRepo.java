package com.freshii.dao.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.freshii.model.Customer;
import com.freshii.model.Order;

public interface OrderRepo extends MongoRepository<Order, String>{
    @Query("{customerId:'?0',orderStatus:'UNPAID'}")
    Order findUnPaidOrderOfCustomer(String customerId);
    
    @Query("{customerId:'?0'}")
    List<Order> findOrdersOfCustomer(String customerId);
    
    @Query("{phone:'?0',number:'?1'}")
    Order findOrderOfCustomerByPhoneAndNumber(String phone, int number);
}
