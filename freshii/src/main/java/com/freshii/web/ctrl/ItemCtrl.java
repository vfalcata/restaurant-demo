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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freshii.dao.ItemService;
import com.freshii.dao.ManagerService;
import com.freshii.model.Item;
import com.freshii.model.Manager;

@CrossOrigin
@RestController
public class ItemCtrl {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ManagerService managerService;
	
	private static final String ID_LABEL = "id";
	private static final String NAME_LABEL = "name";
	private static final String CATEGORY_LABEL = "category";
	private static final String MANAGER_ROLE = "MANAGER";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String ROLE_LABEL = "role";
	
	@GetMapping("/item/{id}")
	ResponseEntity<?> getItemById(@PathVariable(ID_LABEL) String id) {
		Item item = itemService.getItemById(id);
		if (item == null) {
			return new ResponseEntity<Error>(new Error("item not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Item>(item, HttpStatus.OK);
	}
	
	@GetMapping("/item/name/{name}")
	ResponseEntity<?> getItemByName(@PathVariable(NAME_LABEL) String name) {
		Item item = itemService.getItemByName(name);
		if (item == null) {
			return new ResponseEntity<Error>(new Error("item not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Item>(item, HttpStatus.OK);
	}
	
	@GetMapping("/item/category/{category}")
	ResponseEntity<?> getItemByCategory(@PathVariable(CATEGORY_LABEL) String category) {
		Item item = itemService.getItemByCategory(category);
		if (item == null) {
			return new ResponseEntity<Error>(new Error("item not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Item>(item, HttpStatus.OK);
	}
	
	@GetMapping("/items")
	ResponseEntity<?> getItems() {
		return new ResponseEntity<>(itemService.getItems(), HttpStatus.OK);
	}
	
	@PutMapping("/item")
	ResponseEntity<?> createItem(@RequestBody Item item, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticateManager(username, password, role)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		String itemId = item.getId();
		if(itemId!=null && itemService.getItemById(item.getId())!=null) {
			return new ResponseEntity<Error>(new Error("failed to create item, already exists"), HttpStatus.CONFLICT);

		}

		Item newItem = itemService.createItem(item);
		if (newItem == null) {
			return new ResponseEntity<Error>(new Error("failed to create item"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Item>(itemService.createItem(item), HttpStatus.CREATED);
	}

	@DeleteMapping("/item/{id}")
	ResponseEntity<?> deleteItem(@PathVariable(ID_LABEL) String id, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticateManager(username, password, role)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		if (itemService.getItemById(id) == null) {
			return new ResponseEntity<Error>(new Error("Item does not exist"), HttpStatus.NOT_FOUND);
		}
		Item deletedItem = itemService.deleteItem(id);
		if (deletedItem == null) {
			return new ResponseEntity<Error>(new Error("Failed to delete item"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(deletedItem, HttpStatus.OK);
	}

	@PatchMapping("/item")
	ResponseEntity<?> editItem(@RequestBody Item item, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticateManager(username, password, role)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		Item newItem = itemService.getItemById(item.getId());
		if (newItem == null) {
			return new ResponseEntity<Error>(new Error("failed to update item, it does not yet exist"),
					HttpStatus.NOT_FOUND);
		}

		newItem = itemService.updateItem(item);

		if (newItem == null) {
			return new ResponseEntity<Error>(new Error("failed to update item"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Item>(newItem, HttpStatus.OK);
	}
	
	private boolean authenticateManager(String username, String password, String role) {
		Manager manager = managerService.loginManager(username, password);
		return manager != null && role.equals(MANAGER_ROLE);
	}



}
