package com.freshii.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.freshii.ctrl.fixtures.MockCookieRequest;
import com.freshii.dao.AdminService;
import com.freshii.model.Manager;
import com.freshii.web.ctrl.AdminCtrl;
import javax.servlet.http.Cookie;

@WebMvcTest(controllers = AdminCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminCtrlTest {

	private static final String ROLE = "ADMIN";
	private static final String ID_LABEL = "id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";


	@Value("#{systemEnvironment['ADMIN_USERNAME'] ?: 'admin'}")
	private String ADMIN_USERNAME;

	@Value("#{systemEnvironment['ADMIN_PASSWORD'] ?: '4413'}")
	private String ADMIN_PASSWORD;

	@MockBean
	private AdminService mockAdminService;

	@Autowired
	private MockMvc mockMvc;

	private List<Manager> managers;

	private static final String role = "ADMIN";

	private Cookie cookie;

	@BeforeEach
	public void setup() {
		this.managers = new ArrayList<>();
		managers.add(new Manager.Builder("mockManager1", "mockpass1").id("123").build());
		managers.add(new Manager.Builder("mockManager2", "mockpass2").id("456").build());
		managers.add(new Manager.Builder("mockManager3", "mockpass3").id("789").build());
		managers.add(new Manager.Builder("mockManager4", "mockpass4").id("987").build());
		managers.add(new Manager.Builder("mockManager5", "mockpass5").id("654").build());
	}



	@Test
	@DisplayName("Should list all managers")
	public void shouldListAllManagers() throws Exception {
		Mockito.when(mockAdminService.getManagers()).thenReturn(managers);
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/managers",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(jsonPath("$.*", Matchers.hasSize(5)))
				.andExpect(jsonPath("$[*]." + ID_LABEL, Matchers.containsInAnyOrder("123", "456", "789", "987", "654")))
				.andExpect(jsonPath("$[*]." + USERNAME_LABEL,
						Matchers.containsInAnyOrder("mockManager1", "mockManager2", "mockManager3", "mockManager4",
								"mockManager5")))
				.andExpect(jsonPath("$[*]." + PASSWORD_LABEL,
						Matchers.containsInAnyOrder("mockpass1", "mockpass2", "mockpass3", "mockpass4", "mockpass5")))
				.andExpect(status().is(200)).andDo(print());

	}

	@Test
	@DisplayName("Should not list all managers")
	public void shouldNotListAllManagers() throws Exception {
		Mockito.when(mockAdminService.getManagers()).thenReturn(managers);
		// unauthorized request
		Mockito.when(mockAdminService.getManager("999")).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookieInvalidGetRequest("/admin/managers",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(401)).andDo(print());
	}

	@Test
	@DisplayName("Should login admin with correct credentials")
	public void shouldLoginAdmin() throws Exception {
		mockMvc.perform(MockCookieRequest.cookiePostRequest("/admin/login",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \"admin\",\"password\": \"4413\"}").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value(ADMIN_USERNAME))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value(ADMIN_PASSWORD)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Should not login admin with wrong credentials")
	public void shouldNotLoginAdmin() throws Exception {
		mockMvc.perform(MockCookieRequest.cookiePostRequest("/admin/login",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \"wrongadmin\",\"password\": \"wrongpass4413\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should get manager")
	public void shouldGetManagerById() throws Exception {
		Mockito.when(mockAdminService.getManager("123")).thenReturn(managers.get(0));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/123",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager1"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass1")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("456")).thenReturn(managers.get(1));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/456",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager2"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass2")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("789")).thenReturn(managers.get(2));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/789",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager3"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass3")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("987")).thenReturn(managers.get(3));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/987",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager4"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass4")).andExpect(status().is(200))
				.andDo(print());
		Mockito.when(mockAdminService.getManager("654")).thenReturn(managers.get(4));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/654",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager5"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass5")).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not find non existent manager")
	public void shouldNotGetManagerById() throws Exception {
		// manager does not exist
		Mockito.when(mockAdminService.getManager("999")).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/999",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());

		// unauthorized request
		Mockito.when(mockAdminService.getManager("999")).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookieInvalidGetRequest("/admin/manager/999",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(401)).andDo(print());
	}

	@Test
	@DisplayName("Should create manager")
	public void shouldCreateManager() throws Exception {
		Manager newManager = new Manager.Builder("mockManager6", "mockpass6").id("999").build();
		Mockito.when(mockAdminService.createManager(any(Manager.class))).thenReturn(newManager);
		managers.add(newManager);
		mockMvc.perform(MockCookieRequest.cookiePutRequest("/admin/manager/create",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"999\",\"username\": \"mockManager6\",\"password\": \"mockpass6\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + ID_LABEL).value("999"))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager6"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass6")).andExpect(status().isCreated());

		// ensure overall, all manager properties
		Mockito.when(mockAdminService.getManagers()).thenReturn(managers);
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/managers",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"username\": \"testmanager\",\"password\": \"testmanagerpassword\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(jsonPath("$.*", Matchers.hasSize(6)))
				.andExpect(jsonPath("$[*]." + ID_LABEL,
						Matchers.containsInAnyOrder("123", "456", "789", "987", "654", "999")))
				.andExpect(jsonPath("$[*]." + USERNAME_LABEL,
						Matchers.containsInAnyOrder("mockManager1", "mockManager2", "mockManager3", "mockManager4",
								"mockManager5", "mockManager6")))
				.andExpect(jsonPath("$[*]." + PASSWORD_LABEL, Matchers.containsInAnyOrder("mockpass1", "mockpass2",
						"mockpass3", "mockpass4", "mockpass5", "mockpass6")))
				.andExpect(status().is(200));

		// ensure created manager exists
		Mockito.when(mockAdminService.getManager(anyString())).thenReturn(managers.get(5));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/654",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("999"))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager6"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass6")).andExpect(status().is(200))
				.andDo(print());

		// ensure all other managers unchanged
		Mockito.when(mockAdminService.getManager("123")).thenReturn(managers.get(0));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/123",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager1"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass1")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("456")).thenReturn(managers.get(1));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/456",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager2"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass2")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("789")).thenReturn(managers.get(2));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/789",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager3"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass3")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("987")).thenReturn(managers.get(3));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/987",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager4"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass4")).andExpect(status().is(200))
				.andDo(print());

		Mockito.when(mockAdminService.getManager("654")).thenReturn(managers.get(4));
		mockMvc.perform(MockCookieRequest.cookieGetRequest("/admin/manager/654",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager5"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass5")).andExpect(status().is(200))
				.andDo(print());

	}

	@Test
	@DisplayName("Should not create manager")
	public void shouldNotCreateManager() throws Exception {
		// manager already exist
		Manager newManager = new Manager.Builder("mockManager999", "mockpass999").id("999").build();
		Mockito.when(mockAdminService.getManager(anyString())).thenReturn(newManager);
		mockMvc.perform(MockCookieRequest.cookiePutRequest("/admin/manager/create",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"999\",\"username\": \"mockManager999\",\"password\": \"mockpass999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict()).andDo(print());

		// manager creation failed
		Mockito.when(mockAdminService.getManager(anyString())).thenReturn(null);
		Mockito.when(mockAdminService.createManager(any(Manager.class))).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookiePutRequest("/admin/manager/create",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

		// unauthorized request
		Mockito.when(mockAdminService.createManager(any(Manager.class)))
				.thenReturn(new Manager.Builder("mockManager6", "mockpass6").id("999").build());
		mockMvc.perform(MockCookieRequest.cookieInvalidPutRequest("/admin/manager/create",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"999\",\"username\": \"mockManager6\",\"password\": \"mockpass6\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(401)) // UNAUTHORIZED code is 401
				.andDo(print());
	}

	@Test
	@DisplayName("Should delete manager")
	public void shouldDeleteManager() throws Exception {
		Manager manager = new Manager.Builder("mockManager6", "mockpass6").id("999").build();
		Mockito.when(mockAdminService.removeManager("999")).thenReturn(manager);
		Mockito.when(mockAdminService.getManager("999")).thenReturn(manager);
		mockMvc.perform(MockCookieRequest.cookieDeleteRequest("/admin/manager/delete/999",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").value("999"))
				.andExpect(jsonPath("$." + USERNAME_LABEL).value("mockManager6"))
				.andExpect(jsonPath("$." + PASSWORD_LABEL).value("mockpass6")).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("Should not delete manager")
	public void shouldNotDeleteManager() throws Exception {
		Manager manager = new Manager.Builder("mockManager6", "mockpass6").id("999").build();

		// manager not exist
		Mockito.when(mockAdminService.getManager(managers.get(0).getId())).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookieDeleteRequest("/admin/manager/delete/" + managers.get(0).getId(),ADMIN_USERNAME,ADMIN_PASSWORD,ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// manager did not get deleted
		Mockito.when(mockAdminService.getManager(managers.get(0).getId())).thenReturn(managers.get(0));
		Mockito.when(mockAdminService.removeManager(managers.get(0).getId())).thenReturn(null);
		mockMvc.perform(MockCookieRequest.cookieDeleteRequest("/admin/manager/delete/" + managers.get(0).getId(),ADMIN_USERNAME,ADMIN_PASSWORD,ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// unauthorized request
		Mockito.when(mockAdminService.removeManager("999")).thenReturn(manager);
		mockMvc.perform(MockCookieRequest.cookieInvalidDeleteRequest("/admin/manager/delete/999",ADMIN_USERNAME,ADMIN_PASSWORD,ROLE).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().is(401)) // UNAUTHORIZED code is 401
				.andDo(print());
	}

}
