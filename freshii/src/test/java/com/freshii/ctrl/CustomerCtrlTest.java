package com.freshii.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.freshii.dao.CustomerService;
import com.freshii.model.Customer;
import com.freshii.web.ctrl.CustomerCtrl;

@WebMvcTest(controllers = CustomerCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerCtrlTest {
	private static final String ID_LABEL = "id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";

	@MockBean
	private CustomerService mockCustomerService;

	@Autowired
	private MockMvc mockMvc;

	private Customer customer;

	@BeforeEach
	public void setup() {
		this.customer = new Customer.Builder("cust1", "custpass1").id("cuid1").build();
	}

	@Test
	@DisplayName("Should login customer with correct credentials")
	public void shouldLoginCustomer() throws Exception {
		Mockito.when(mockCustomerService.loginCustomer(this.customer.getUsername(),this.customer.getPassword()))
				.thenReturn(this.customer);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + this.customer.getUsername() + "\",\"" + PASSWORD_LABEL
						+ "\": \"" + this.customer.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value(this.customer.getUsername()))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value(this.customer.getPassword()))
				.andExpect(jsonPath("$." + ID_LABEL).value(customer.getId())).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Should not login customer with wrong credentials")
	public void shouldNotLoginCustomer() throws Exception {
		Mockito.when(mockCustomerService.loginCustomer(anyString(), anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/manager/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + customer.getUsername() + "WRONG\",\"" + PASSWORD_LABEL
						+ "WRONG\": \"" + customer.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Should sign up customer")
	public void shouldSignupCustomer() throws Exception {
		Mockito.when(mockCustomerService.signupCustomer(any(Customer.class))).thenReturn(this.customer);
		Mockito.when(mockCustomerService.loginCustomer(this.customer.getUsername(),this.customer.getPassword())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/signup").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + this.customer.getUsername() + "\",\"" + PASSWORD_LABEL
						+ "\": \"" + this.customer.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value(this.customer.getUsername()))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value(this.customer.getPassword()))
				.andExpect(jsonPath("$." + ID_LABEL).value(customer.getId()))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Should not signup")
	public void shouldNotSignupCustomer() throws Exception {
		Mockito.when(mockCustomerService.signupCustomer(any(Customer.class))).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/customer/signup").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + this.customer.getUsername() + "\",\"" + PASSWORD_LABEL
						+ "\": \"" + this.customer.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}
}
