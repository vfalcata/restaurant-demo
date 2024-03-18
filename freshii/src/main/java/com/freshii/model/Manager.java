package com.freshii.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("manager")
public class Manager {
    @Id
    private String id;
    private String username;
    private String password;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void getId(String id) {
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
	
	public Manager() {
		super();
		this.username = "";
		this.password = "";
		this.id = "";

	}
	public static class Builder
	{

		public String username;
		public String password;
	    public String id;
		
		public Builder(String username, String password) {
			this.username = username;
			this.password = password;
		}
		public Builder id(String id) {
			this.id=id;
			return this;
		}

		public Manager build() {
			Manager manager =  new Manager();
			manager.username=this.username;
			manager.password=this.password;
			manager.id=this.id;
			return manager;
		}

	}
}
