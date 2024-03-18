package com.freshii.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.freshii.dao.repo.CustomerRepo;
import com.freshii.dao.repo.OrderRepo;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Order;


@Service
public class CustomerService {
	@Autowired
	private CustomerRepo customerRepo;
	


	public Customer signupCustomer(Customer customer) {
		if(loginCustomer(customer.getUsername(),customer.getPassword())!=null) {
			return null;
		}
		return customerRepo.save(customer);
	}
	
	public Customer loginCustomer(String username, String password) {
		return customerRepo.findCustomerWithUsernameAndPassword(username, password);
	}


}
