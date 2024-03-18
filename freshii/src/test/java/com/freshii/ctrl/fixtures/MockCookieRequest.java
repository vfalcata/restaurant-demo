package com.freshii.ctrl.fixtures;

import javax.servlet.http.Cookie;

import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockCookieRequest {
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String ROLE_LABEL = "role";
	// sends post request with correct username and pass
	public static MockHttpServletRequestBuilder cookiePostRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.post(url).cookie(new Cookie(USERNAME_LABEL, username))
				.cookie(new Cookie(PASSWORD_LABEL, password)).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends post request with wrong username and pass
	public static MockHttpServletRequestBuilder cookieInvalidPostRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.post(url).cookie(new Cookie(USERNAME_LABEL, username + "WRONG"))
				.cookie(new Cookie(PASSWORD_LABEL, password + "WRONG")).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends get request with correct username and pass
	public static MockHttpServletRequestBuilder cookieGetRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.get(url).cookie(new Cookie(USERNAME_LABEL, username))
				.cookie(new Cookie(PASSWORD_LABEL, password)).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends get request with wrong username and pass
	public static MockHttpServletRequestBuilder cookieInvalidGetRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.get(url).cookie(new Cookie(USERNAME_LABEL, username + "WRONG"))
				.cookie(new Cookie(PASSWORD_LABEL, password + "WRONG")).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends delete request with correct username and pass
	public static MockHttpServletRequestBuilder cookieDeleteRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.delete(url).cookie(new Cookie(USERNAME_LABEL, username))
				.cookie(new Cookie(PASSWORD_LABEL, password)).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends delete request with wrong username and pass
	public static MockHttpServletRequestBuilder cookieInvalidDeleteRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.delete(url).cookie(new Cookie(USERNAME_LABEL, username + "WRONG"))
				.cookie(new Cookie(PASSWORD_LABEL, password + "WRONG")).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends put request with correct username and pass
	public static MockHttpServletRequestBuilder cookiePutRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.put(url).cookie(new Cookie(USERNAME_LABEL, username))
				.cookie(new Cookie(PASSWORD_LABEL, password)).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends put request with wrong username and pass
	public static MockHttpServletRequestBuilder cookieInvalidPutRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.put(url).cookie(new Cookie(USERNAME_LABEL, username + "WRONG"))
				.cookie(new Cookie(PASSWORD_LABEL, password + "WRONG")).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends put request with correct username and pass
	public static MockHttpServletRequestBuilder cookiePatchRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.patch(url).cookie(new Cookie(USERNAME_LABEL, username))
				.cookie(new Cookie(PASSWORD_LABEL, password)).cookie(new Cookie(ROLE_LABEL, role));
	}

	// sends put request with wrong username and pass
	public static MockHttpServletRequestBuilder cookieInvalidPatchRequest(String url, String username, String password, String role) {
		return MockMvcRequestBuilders.patch(url).cookie(new Cookie(USERNAME_LABEL, username + "WRONG"))
				.cookie(new Cookie(PASSWORD_LABEL, password + "WRONG")).cookie(new Cookie(ROLE_LABEL, role));
	}
}
