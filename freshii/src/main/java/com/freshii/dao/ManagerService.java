package com.freshii.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshii.dao.repo.ItemRepo;
import com.freshii.dao.repo.ManagerRepo;
import com.freshii.dao.repo.OrderRepo;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;
@Service
public class ManagerService {
	@Autowired
	private ManagerRepo managerRepo;
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private ItemRepo itemRepo;
	
	public Manager loginManager(String username, String password) {
		return managerRepo.findManagerWithUsernameAndPassword(username, password);
	}
	
}
