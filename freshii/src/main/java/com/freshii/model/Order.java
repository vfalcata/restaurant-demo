package com.freshii.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.freshii.model.Customer.Builder;

@Document("order")
public class Order {
    @Id
    private String id;
    private String customerId;
    private String orderStatus;
    private String pickupAddress;
    private String deliveryAddress;
    public static final int MIN_SIZE=2;
    public static final int MAX_SIZE=10;
    private int number;
    private String phone;
    
    public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	@DBRef
    private Map<String, Item> items;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPickupAddress() {
		return pickupAddress;
	}
	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public Map<String, Item> getItems() {
		return items;
	}
	public void setItems(Map<String, Item> items) {
		this.items = items;
	}
	public static int getMinSize() {
		return MIN_SIZE;
	}
	public static int getMaxSize() {
		return MAX_SIZE;
	}
    
	public void setOrderStatusPaid() {
		this.orderStatus="PAID";
	}
	public void setOrderStatusUnpaid() {
		this.orderStatus="UNPAID";
	}
	public void setOrderStatusDelivering() {
		this.orderStatus="DELIVERING";
	}
	public void setOrderStatusSigned() {
		this.orderStatus="SIGNED";
	}
	public void setOrderStatusCancelled() {
		this.orderStatus="CANCELLED";
	}
	
	public static class Builder
	{
	    private String id;
	    private String customerId;
	    private String orderStatus;
	    private String pickupAddress;
	    private String deliveryAddress;
	    private Map<String, Item> items;
	    private int number;
	    private String phone;
	    
	    
		
		public Builder(String customerId) {
			this.customerId = customerId;
			this.items = new HashMap<>();
		}
		
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		public Builder number(int number) {
			this.number = number;
			return this;
		}
		public Builder phone(String phone) {
			this.phone = phone;
			return this;
		}
		public Builder orderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
			return this;
		}
		public Builder pickupAddress(String pickupAddress) {
			this.pickupAddress = pickupAddress;
			return this;
		}
		public Builder deliveryAddress(String deliveryAddress) {
			this.deliveryAddress = deliveryAddress;
			return this;
		}
		public Builder items(Map<String, Item> items) {
			this.items = items;
			return this;
		}
		
		public Order build() {
			Order order =  new Order();
			order.customerId=this.customerId;
			order.orderStatus=this.orderStatus;
			order.pickupAddress=this.pickupAddress;
			order.deliveryAddress=this.deliveryAddress;
			order.items=this.items;
			order.id=this.id;
			order.number=this.number;
			order.phone=this.phone;
			return order;
		}

	}

}
