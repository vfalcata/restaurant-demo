package com.freshii.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.freshii.dao.CustomerService;
import com.freshii.dao.ItemService;
import com.freshii.dao.ManagerService;
import com.freshii.dao.OrderService;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;

@CrossOrigin
@RestController
public class OrderCtrl {
	@Autowired
	private OrderService orderService;
		
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private ItemService itemService;
	

	private static final String MANAGER_ROLE = "MANAGER";
	private static final String CUSTOMER_ROLE = "CUSTOMER";
	private static final String ID_LABEL = "id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String ROLE_LABEL = "role";
	private static final String ITEM_SIZE_LABEL = "size";
	private static final String ITEM_QUANTITY_LABEL = "quantity";

	@PatchMapping("/order/delete/item/{id}")
	ResponseEntity<?> deleteOrderItem(@PathVariable(ID_LABEL) String id,@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer = customerService.loginCustomer(username, password);
		if (!authenticateCustomer(username, password, role)) {	
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		Order order = orderService.deleteOrderItem(customer.getId(),id);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	
	@PatchMapping("/order/update/{id}/{size}/{quantity}")
	ResponseEntity<?> updateOrderItem(@PathVariable(ID_LABEL) String id, @PathVariable(ITEM_SIZE_LABEL) String size, @PathVariable(ITEM_QUANTITY_LABEL) int quantity, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer = customerService.loginCustomer(username, password);
		if (!authenticateCustomer(username, password, role)) {	
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		
		Order order = orderService.updateOrder(customer.getId(), id, size, quantity);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("failed to update"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	
	@PatchMapping("/order/update/{id}/quantity/{quantity}")
	ResponseEntity<?> updateOrderItemQuantity(@PathVariable(ID_LABEL) String id, @PathVariable(ITEM_QUANTITY_LABEL) int quantity,@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer = customerService.loginCustomer(username, password);
		if (!authenticateCustomer(username, password, role)) {	
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		Order order = orderService.updateOrderItemQuantity(customer.getId(),id,quantity);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	

	
	@PatchMapping("/order/update/{id}/size/{size}")
	ResponseEntity<?> updateOrderItemSize(@PathVariable(ID_LABEL) String id, @PathVariable(ITEM_SIZE_LABEL) String size,@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer = customerService.loginCustomer(username, password);
		if (!authenticateCustomer(username, password, role)) {	
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		Order order = orderService.updateOrderItemSize(customer.getId(),id,size);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	
	@PatchMapping("/order/cancel/{id}")
	ResponseEntity<?> cancelOrder(@PathVariable(ID_LABEL) String id, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (!authenticateCustomer(username, password, role)) {	
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		
		Order order = orderService.getOrder(id);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("failed to update"), HttpStatus.NOT_FOUND);
		}
		
		if(order.getOrderStatus().equals("SIGNED") || order.getOrderStatus().equals("DELIVERING")|| order.getOrderStatus().equals("UNPAID")) {
			return new ResponseEntity<Error>(new Error("failed to update, order with this status cannot be cancelled"), HttpStatus.BAD_REQUEST);
		}
		order.setOrderStatusCancelled();
		order=orderService.saveOrder(order);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("failed to cancel order"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	
	@PatchMapping("/order/nextstep/{id}")
	ResponseEntity<?> proceedOrderStatus(@PathVariable(ID_LABEL) String id,@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Order order = orderService.getOrder(id);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("failed to update"), HttpStatus.NOT_FOUND);
		}
		if(order.getOrderStatus().equals("UNPAID")) {
			if (!authenticateCustomer(username, password, role)) {	
				return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
			}
			order.setOrderStatusPaid();
		}else if(order.getOrderStatus().equals("PAID")) {
			if (!authenticateManager(username, password, role)) {	
				return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
			}
			order.setOrderStatusDelivering();
		}else if(order.getOrderStatus().equals("DELIVERING")) {
			if (!authenticateCustomer(username, password, role)) {
				return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
			}
			order.setOrderStatusSigned();
		}else if(order.getOrderStatus().equals("SIGNED")) {
			if (!authenticateCustomer(username, password, role)) {	
				return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
			}
			return new ResponseEntity<Error>(new Error("order already fufilled"), HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<Error>(new Error("No more next steps for order"), HttpStatus.BAD_REQUEST);
		}
		order = orderService.saveOrder(order);
		if(order == null) {
			return new ResponseEntity<Error>(new Error("failed to update satus 4"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Order>(order , HttpStatus.OK);
	}
	
	
	@GetMapping("/order/{id}")
	ResponseEntity<?> getOrderById(@PathVariable(ID_LABEL) String id, @CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		if (role.equals(MANAGER_ROLE) && !authenticateManager(username, password, role)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		if (role.equals(CUSTOMER_ROLE) && !authenticateCustomer(username, password, role)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		Order order = orderService.getOrder(id);
		if (order == null) {
			return new ResponseEntity<Error>(new Error("Order dose not exist"), HttpStatus.NOT_FOUND);
		}
		if (order != null && role.equals(CUSTOMER_ROLE) && order.getCustomerId().equals(customerService.loginCustomer(username, password).getId())) {
			return new ResponseEntity<Error>(new Error("Order dose not exist"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
	
	@GetMapping("/order")
	ResponseEntity<?> getOrder(@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer=customerService.loginCustomer(username, password);
		if (customer==null) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		
		Order order = orderService.getCustomerUnpaidOrder(customer.getId());
		if (order == null) {
			order = orderService.createOrder(customer.getId());
		}
		
		if (order == null) {
			return new ResponseEntity<Error>(new Error("Order dose not exist"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}
	
	@GetMapping("/orders")
	ResponseEntity<?> getOrders(@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer=customerService.loginCustomer(username, password);
		if (customer==null || !role.equals(CUSTOMER_ROLE)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		
		List<Order> orders = orderService.getOrders(customer.getId());
		if (orders == null) {
			return new ResponseEntity<Error>(new Error("No orders found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/order/phone/{phone}/number/{number}")
	ResponseEntity<?> getOrders(@PathVariable("phone") String phone,@PathVariable("number") int number,@CookieValue(USERNAME_LABEL) String username,
			@CookieValue(PASSWORD_LABEL) String password, @CookieValue(ROLE_LABEL) String role) {
		Customer customer=customerService.loginCustomer(username, password);
		if (customer==null || !role.equals(CUSTOMER_ROLE)) {
			return new ResponseEntity<Error>(new Error("failed to authenticate"), HttpStatus.UNAUTHORIZED);
		}
		
		Order order = orderService.getOrderByPhoneAndNumber(phone,number);
		if (order == null) {
			return new ResponseEntity<Error>(new Error("No order found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(order, HttpStatus.OK);
	}
	
	private boolean authenticateManager(String username, String password, String role) {
		Manager manager = managerService.loginManager(username, password);
		return manager != null && role.equals(MANAGER_ROLE);
	}
	private boolean authenticateCustomer(String username, String password, String role) {
		Customer customer = customerService.loginCustomer(username, password);
		return customer != null && role.equals(CUSTOMER_ROLE);
	}

}
