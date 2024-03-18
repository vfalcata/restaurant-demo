package com.freshii.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freshii.dao.AdminService;
import com.freshii.model.Admin;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;

@CrossOrigin
@RestController
public class AdminCtrl {

	@Value("#{systemEnvironment['ADMIN_USERNAME'] ?: 'admin'}")
	private String adminUsername;

	@Value("#{systemEnvironment['ADMIN_PASSWORD'] ?: '4413'}")
	private String adminPassword;

	private static final String ROLE = "ADMIN";

	private static final String ID_LABEL = "id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String ROLE_LABEL = "role";

	@Autowired
	private AdminService adminService;

	@PostMapping("/admin/login")
	ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
		if (authenticate(admin.getUsername(), admin.getPassword())) {
			return new ResponseEntity<Admin>(new Admin.Builder(admin.getUsername(), admin.getPassword()).build(),
					HttpStatus.OK);
		}
		return new ResponseEntity<Error>(new Error("failed to sign up try again"), HttpStatus.NOT_FOUND);
	}

	@GetMapping("/admin/manager/{id}")
	ResponseEntity<?> getManager(@PathVariable(ID_LABEL) String id, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticate(username, password, role)) {
			return new ResponseEntity<Error>(new Error("admin credentials incorrect"), HttpStatus.UNAUTHORIZED);
		}
		Manager manager = adminService.getManager(id);
		if (manager == null) {
			return new ResponseEntity<Error>(new Error("manager not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Manager>(adminService.getManager(id), HttpStatus.OK);
	}

	@DeleteMapping("/admin/manager/delete/{id}")
	ResponseEntity<?> deleteManager(@PathVariable(ID_LABEL) String id, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticate(username, password, role)) {
			return new ResponseEntity<Error>(new Error("admin credentials incorrect"), HttpStatus.UNAUTHORIZED);
		}

		if (adminService.getManager(id) == null) {
			return new ResponseEntity<Error>(new Error("failed to find manager"), HttpStatus.NOT_FOUND);
		}

		Manager manager = adminService.removeManager(id);
		if (manager == null) {
			return new ResponseEntity<Error>(new Error("failed to delete manager"), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Manager>(manager, HttpStatus.OK);
	}

	@PutMapping("/admin/manager/create")
	ResponseEntity<?> createManager(@RequestBody Manager manager, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticate(username, password, role)) {
			return new ResponseEntity<Error>(new Error("admin credentials incorrect"), HttpStatus.UNAUTHORIZED);
		}

		Manager newManager = adminService.getManager(manager.getId());
		if (newManager != null) {
			return new ResponseEntity<Error>(new Error("failed to create manager, username already taken"),
					HttpStatus.CONFLICT);
		}

		newManager = adminService.createManager(manager);

		if (newManager == null) {
			return new ResponseEntity<Error>(new Error("failed to create manager"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Manager>(newManager, HttpStatus.CREATED);
	}

	@GetMapping("/admin/managers")
	ResponseEntity<?> getManagers(@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (authenticate(username, password, role)) {
			return new ResponseEntity<List<Manager>>(adminService.getManagers(), HttpStatus.OK);
		}
		return new ResponseEntity<Error>(new Error("admin credentials incorrect"), HttpStatus.UNAUTHORIZED);
	}

	private boolean authenticate(String username, String password) {
		return username.equals(this.adminUsername) && password.equals(this.adminPassword);
	}

	private boolean authenticate(String username, String password, String role) {
		return authenticate(username, password) && role.equals(this.ROLE);
	}
}
