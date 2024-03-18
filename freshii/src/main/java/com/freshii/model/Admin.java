package com.freshii.model;

import java.util.Map;

import org.springframework.data.annotation.Id;

import com.freshii.model.Customer.Builder;

public class Admin {
	private String username;
	private String password;
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
	

	public static class Builder
	{

		public String username;
		public String password;

		
		public Builder(String username, String password) {
			this.username = username;
			this.password = password;
		}


		public Admin build() {
			Admin admin =  new Admin();
			admin.username=this.username;
			admin.password=this.password;
			return admin;
		}

	}
}
