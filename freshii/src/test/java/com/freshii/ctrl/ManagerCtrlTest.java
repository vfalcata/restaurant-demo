package com.freshii.ctrl;

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
import com.freshii.dao.ManagerService;
import com.freshii.model.Manager;
import com.freshii.web.ctrl.ManagerCtrl;

@WebMvcTest(controllers = ManagerCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class ManagerCtrlTest {

	private static final String ID_LABEL = "id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";

	@MockBean
	private ManagerService mockManagerService;

	@Autowired
	private MockMvc mockMvc;

	private Manager manager;

	@BeforeEach
	public void setup() {
		this.manager = new Manager.Builder("mockManager1", "mockpass1").id("123").build();
	}

	@Test
	@DisplayName("Should login admin with correct credentials")
	public void shouldLoginManager() throws Exception {
		Mockito.when(mockManagerService.loginManager(this.manager.getUsername(),this.manager.getPassword()))
				.thenReturn(this.manager);
		mockMvc.perform(MockMvcRequestBuilders.post("/manager/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + this.manager.getUsername() + "\",\"" + PASSWORD_LABEL
						+ "\": \"" + this.manager.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value(this.manager.getUsername()))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value(this.manager.getPassword()))
				.andExpect(jsonPath("$." + ID_LABEL).value(manager.getId())).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Should not login manager with wrong credentials")
	public void shouldNotLoginAdmin() throws Exception {
		Mockito.when(mockManagerService.loginManager(anyString(), anyString())).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.post("/manager/login").contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + USERNAME_LABEL + "\": \"" + manager.getUsername() + "WRONG\",\"" + PASSWORD_LABEL
						+ "WRONG\": \"" + manager.getPassword() + "\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}


}
