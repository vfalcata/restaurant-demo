package com.freshii.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document("customer")
public class Customer {
    @Id
    private String id;

    private String username;
    private String password;
    private String phoneNumber;
    private String address;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
	public Customer() {
		super();

		this.username = "";
		this.password = "";
		this.phoneNumber = "";
		this.address = "";
	}
	
	private Customer(Builder builder) {
		super();

		this.username = builder.username;
		this.password = builder.password;
		this.phoneNumber = builder.phoneNumber;
		this.address = builder.address;
	}
	
	public static class Builder
	{

		public String username;
		public String password;
		public String phoneNumber;
		public String address;
		   private String id;
		
		public Builder(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public Builder id(String id) {
			this.id = id;
			return this;
		}
		

		public Builder address(String address) {
			this.address = address;
			return this;
		}
		public Builder phoneNumber(String phoneNumber) {
			this.phoneNumber = address;
			return this;
		}

		public Customer build() {
			Customer customer =  new Customer(this);
			return customer;
		}

	}
}
