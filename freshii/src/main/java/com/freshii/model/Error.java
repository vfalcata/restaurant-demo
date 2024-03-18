package com.freshii.model;
//based off https://stackoverflow.com/questions/55789337/best-practice-to-send-response-in-spring-boot/55791293#55791293


public class Error
{
    public Error(String message) {
        this.message = message;

    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;


}