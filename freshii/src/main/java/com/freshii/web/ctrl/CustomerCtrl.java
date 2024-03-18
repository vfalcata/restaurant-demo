package com.freshii.web.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freshii.dao.CustomerService;
import com.freshii.model.Customer;

@CrossOrigin
@RestController
public class CustomerCtrl {
	@Autowired
	private CustomerService customerService;

	@PostMapping("/customer/signup")
	ResponseEntity<?> newCustomer(@RequestBody Customer customer) {
		Customer signupCustomer = customerService.signupCustomer(customer);
	    if(signupCustomer == null) {
	    	return new ResponseEntity<Error>(new Error("failed to sign up try again"), HttpStatus.CONFLICT);
	    }
	    return new ResponseEntity<Customer>(signupCustomer, HttpStatus.CREATED);
    }

	@PostMapping("/customer/login")
	ResponseEntity<?> loginCustomer(@RequestBody Customer customer) {
		Customer loginCustomer = customerService.loginCustomer(customer.getUsername(), customer.getPassword());
	    if(loginCustomer == null) {
	    	return new ResponseEntity<Error>(new Error("failed to login try again"), HttpStatus.NOT_FOUND);
	    }
	    return new ResponseEntity<Customer>(loginCustomer, HttpStatus.OK);

    }

	
	
}
