package com.freshii.web.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freshii.dao.CustomerService;
import com.freshii.dao.ManagerService;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;

@CrossOrigin
@RestController
public class ManagerCtrl {
	@Autowired
	private ManagerService managerService;


	@PostMapping("/manager/login")
	ResponseEntity<?> loginManager(@RequestBody Manager manager) {
		Manager loginManager = managerService.loginManager(manager.getUsername(), manager.getPassword());
		if (loginManager == null) {
			return new ResponseEntity<Error>(new Error("failed to login try again"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Manager>(loginManager, HttpStatus.OK);
	}


}
