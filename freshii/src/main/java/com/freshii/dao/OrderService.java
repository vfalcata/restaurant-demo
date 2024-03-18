package com.freshii.dao;

import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshii.dao.repo.CustomerRepo;
import com.freshii.dao.repo.ItemRepo;
import com.freshii.dao.repo.OrderRepo;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;

@Service
public class OrderService {
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private ItemService itemService;
	
	public Order addItem(String customerId, String itemId, String size, int quantity) {
		Order order = orderRepo.findUnPaidOrderOfCustomer(customerId);
    	if(order==null) {
    		order=orderRepo.save(new Order.Builder(customerId).orderStatus("UNPAID").build());	
    	}
    	
    	Item item = itemService.getItemById(itemId);
    	if(item==null) {
    		return null;	
    	}
    	item.setSize(size);
    	item.setQuantity(quantity);
		order.getItems().put(itemId, item);
		return orderRepo.save(order);
	}
	
	
	
	public Order getCustomerUnpaidOrder(String customerId) {
		return orderRepo.findUnPaidOrderOfCustomer(customerId);
	}
	
	public Order createOrder(String customerId) {
		return orderRepo.save(new Order.Builder(customerId).build());
	}
	
	public Order updateOrder(String customerId, String itemId, String size, int quantity) {
		Order order = orderRepo.findUnPaidOrderOfCustomer(customerId);
    	if(order==null) {
    		order=orderRepo.save(new Order.Builder(customerId).orderStatus("UNPAID").build());	
    	}
    	
    	Item item = order.getItems().get(itemId);
    	if(item==null) {
    		item=itemService.getItemById(itemId);	
    	}
    	
    	if(item==null) {
    		return null;
    	}
    	
    	if(quantity==0) {
    		order.getItems().remove(itemId);
    		return orderRepo.save(order);
    	}
    	item.setSize(size);
    	item.setQuantity(quantity);
		order.getItems().put(itemId, item);
		return orderRepo.save(order);
	}
	
	public Order deleteOrderItem(String customerId, String itemId) {
		Order order = orderRepo.findUnPaidOrderOfCustomer(customerId);
    	if(order==null) {
    		order=orderRepo.save(new Order.Builder(customerId).orderStatus("UNPAID").build());
    		return null;
    	}
    	
    	Item item = order.getItems().get(itemId);
    	if(item==null) {
    		return null;
    	}
		order.getItems().remove(itemId);
		return orderRepo.save(order);
	}
	
	public Order updateOrderItemQuantity(String customerId, String itemId, int quantity) {
		Order order = orderRepo.findUnPaidOrderOfCustomer(customerId);
    	if(order==null) {
    		order=orderRepo.save(new Order.Builder(customerId).orderStatus("UNPAID").build());	
    	}
    	
    	Item item = order.getItems().get(itemId);
    	if(item==null) {
    		item=itemService.getItemById(itemId);	
        	if(item!=null) {
        		item.setSize("medium");;
        	}
    	}
    	
    	if(item==null) {
    		return null;
    	}
    	
    	item.setQuantity(quantity);
		order.getItems().put(itemId, item);
		return orderRepo.save(order);
	}
	
	public Order updateOrderItemSize(String customerId, String itemId, String size) {
		Order order = orderRepo.findUnPaidOrderOfCustomer(customerId);
    	if(order==null) {
    		order=orderRepo.save(new Order.Builder(customerId).orderStatus("UNPAID").build());
    		return null;
    	}
    	
    	Item item = order.getItems().get(itemId);
    	if(item==null) {
    		item=itemService.getItemById(itemId);	
        	if(item!=null) {
        		item.setQuantity(1);;
        	}
    	}
    	
    	if(item==null) {
    		return null;
    	}
    	item.setSize(size);
		order.getItems().put(itemId, item);
		return orderRepo.save(order);
	}
	
	public Order getOrder(String orderId) {
		return orderRepo.findById(orderId).orElse(null);
	}
	
	public Order getOrderByPhoneAndNumber(String phone, int number) {
		return orderRepo.findOrderOfCustomerByPhoneAndNumber(phone, number);
	}
	
	public Order saveOrder(Order order) {
		return orderRepo.save(order);
	}
	public List<Order> getOrders(String customerId){
		return orderRepo.findOrdersOfCustomer(customerId);
	}
	

}
