package com.freshii.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshii.dao.repo.ManagerRepo;
import com.freshii.model.Customer;
import com.freshii.model.Manager;


@Service
public class AdminService {
	@Autowired
	private ManagerRepo managerRepo;
	
	public Manager removeManager(String managerId) {
		Manager manager = managerRepo.findById(managerId).orElse(null);
		if(manager==null) {
			return null;
		}
		managerRepo.deleteById(managerId);
		//if manager is still there after deletion, then deletion did not happen
		manager = managerRepo.findById(managerId).orElse(null);
		if(manager!=null) {
			return null;
		}
		
		return manager;
		
	}

	public List<Manager> getManagers() {
		return managerRepo.findAll();
	}
	
	public Manager getManager(String managerId) {
		return managerRepo.findById(managerId).orElse(null);
	}
	
	public Manager getManagerWithUsernameAndPassword(String username, String password) {
			return managerRepo.findManagerWithUsernameAndPassword(username, password);
	}
	
	public Manager createManager(Manager manager) {
		if(managerRepo.findManagerWithUsernameAndPassword(manager.getUsername(), manager.getPassword())!=null) {
			return null;
		}
		return managerRepo.save(manager);
	}
	


}
