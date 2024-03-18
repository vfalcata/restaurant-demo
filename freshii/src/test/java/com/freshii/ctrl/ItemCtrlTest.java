package com.freshii.ctrl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.freshii.ctrl.fixtures.MockCookieRequest;
import com.freshii.dao.AdminService;
import com.freshii.dao.ItemService;
import com.freshii.dao.ManagerService;
import com.freshii.dao.repo.ManagerRepo;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;
import com.freshii.web.ctrl.ItemCtrl;
import com.freshii.web.ctrl.ManagerCtrl;

@WebMvcTest(controllers = ItemCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemCtrlTest {
	private static final String MANAGER_ROLE = "MANAGER";
	private static final String ID_LABEL = "id";
	private static final String ITEM_NAME_LABEL = "name";
	private static final String ITEM_CATEGORY_LABEL = "category";
	@MockBean
	private ItemService mockItemService;
	@MockBean
	private ManagerService mockManagerService;
	@Autowired
	private MockMvc mockMvc;
	private List<Item> items;
	private Manager manager;


	@BeforeEach
	public void setup() {
		this.items = new ArrayList<>();
		items.add(new Item.Builder().id("itemid1").name("itemname1").category("itemcategory1").build());
		items.add(new Item.Builder().id("itemid2").name("itemname2").category("itemcategory2").build());
		items.add(new Item.Builder().id("itemid3").name("itemname3").category("itemcategory3").build());
		items.add(new Item.Builder().id("itemid4").name("itemname4").category("itemcategory4").build());
		items.add(new Item.Builder().id("itemid5").name("itemname5").category("itemcategory5").build());
		this.manager = new Manager.Builder("mockManager1", "mockpass1").id("123").build();
	}

	@Test
	@DisplayName("Should get item by id")
	public void shouldGetItemById() throws Exception {
		Mockito.when(mockItemService.getItemById("itemid1")).thenReturn(items.get(0));
		mockMvc.perform(MockMvcRequestBuilders.get("/item/itemid1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + ID_LABEL).value("itemid1"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemname1"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemcategory1")).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not get item by id")
	public void shouldNotGetItemById() throws Exception {
		Mockito.when(mockItemService.getItemById("itemid1")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/item/itemid1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	@DisplayName("Should get item by name")
	public void shouldGetItemByName() throws Exception {
		Mockito.when(mockItemService.getItemByName("itemname1")).thenReturn(items.get(0));
		mockMvc.perform(MockMvcRequestBuilders.get("/item/name/itemname1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + ID_LABEL).value("itemid1"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemname1"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemcategory1")).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not get item by name")
	public void shouldNotGetItemByName() throws Exception {
		Mockito.when(mockItemService.getItemByName("itemname1")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/item/name/itemname1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	@DisplayName("Should get items")
	public void shouldGetItems() throws Exception {
		Mockito.when(mockItemService.getItems()).thenReturn(this.items);
		mockMvc.perform(MockMvcRequestBuilders.get("/items").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(jsonPath("$.*", Matchers.hasSize(5)))
				.andExpect(jsonPath("$[*]." + ID_LABEL,
						Matchers.containsInAnyOrder("itemid1", "itemid2", "itemid3", "itemid4", "itemid5")))
				.andExpect(jsonPath("$[*]." + ITEM_NAME_LABEL,
						Matchers.containsInAnyOrder("itemname1", "itemname2", "itemname3", "itemname4", "itemname5")))
				.andExpect(jsonPath("$[*]." + ITEM_CATEGORY_LABEL, Matchers.containsInAnyOrder("itemcategory1",
						"itemcategory2", "itemcategory3", "itemcategory4", "itemcategory5")))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	@DisplayName("Should get item by category")
	public void shouldGetItemByCategory() throws Exception {
		Mockito.when(mockItemService.getItemByCategory("itemcategory1")).thenReturn(items.get(0));
		mockMvc.perform(MockMvcRequestBuilders.get("/item/category/itemcategory1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("itemid1"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemname1"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemcategory1")).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not get item by category")
	public void shouldNotGetItemByCategory() throws Exception {
		Mockito.when(mockItemService.getItemByCategory("itemcategory1")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get("/item/category/itemcategory1")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	@DisplayName("Should delete item")
	public void shouldDeleteItem() throws Exception {
		Mockito.when(mockItemService.deleteItem("itemid1")).thenReturn(items.get(0));
		Mockito.when(mockItemService.getItemById("itemid1")).thenReturn(items.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieDeleteRequest("/item/itemid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("itemid1"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemname1"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemcategory1")).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not delete item")
	public void shouldNotDeleteItem() throws Exception {
		// item does not exist
		Mockito.when(mockItemService.getItemById("itemid999")).thenReturn(null);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieDeleteRequest("/item/itemid999", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// item did not get deleted
		Mockito.when(mockItemService.deleteItem("itemid1")).thenReturn(null);
		Mockito.when(mockItemService.getItemById("itemid1")).thenReturn(items.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieDeleteRequest("/item/itemid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// poor authentication
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidDeleteRequest("/item/itemid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should create item")
	public void shouldCreateItem() throws Exception {
		Mockito.when(mockItemService.createItem(any(Item.class)))
				.thenReturn(new Item.Builder().id("itemId999").name("itemName999").category("itemCategory999").build());
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(
				MockCookieRequest.cookiePutRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
								+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("itemId999"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemName999"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemCategory999"))
				.andExpect(status().isCreated()).andDo(print());
	}

	@Test
	@DisplayName("Should not create item")
	public void shouldNotCreateItem() throws Exception {
		Item newItem = new Item.Builder().id("itemId999").name("itemName999").category("itemCategory999").build();
		// item does exists
		Mockito.when(mockItemService.getItemById(anyString())).thenReturn(newItem);
		Mockito.when(mockItemService.createItem(any(Item.class))).thenReturn(newItem);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(
				MockCookieRequest.cookiePutRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
								+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict()).andDo(print());

		// item did not create
		Mockito.when(mockItemService.getItemById(anyString())).thenReturn(null);
		Mockito.when(mockItemService.createItem(any(Item.class))).thenReturn(null);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(
				MockCookieRequest.cookiePutRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
								+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// poor authentication
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPutRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
						+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should edit item")
	public void shouldEditItem() throws Exception {
		Item editedItem = new Item.Builder().id("itemId9999").name("itemName9999").category("itemCategory9999").build();
		Mockito.when(mockItemService.updateItem(any(Item.class))).thenReturn(editedItem);
		Mockito.when(mockItemService.getItemById("itemId9999")).thenReturn(editedItem);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + ID_LABEL + "\": \"itemId9999\",\"" + ITEM_NAME_LABEL + "\": \"itemName9999\",\""
						+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory9999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$." + ID_LABEL).value("itemId9999"))
				.andExpect(jsonPath("$." + ITEM_NAME_LABEL).value("itemName9999"))
				.andExpect(jsonPath("$." + ITEM_CATEGORY_LABEL).value("itemCategory9999")).andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	@DisplayName("Should not edit item")
	public void shouldNotEditItem() throws Exception {
		Item updateItem = new Item.Builder().id("itemId999").name("itemName999").category("itemCategory999").build();
		// item does exists
		Mockito.when(mockItemService.getItemById(anyString())).thenReturn(null);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
						+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());

		// item did not create
		Mockito.when(mockItemService.getItemById(anyString())).thenReturn(updateItem);
		Mockito.when(mockItemService.updateItem(any(Item.class))).thenReturn(null);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
						+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());

		// poor authentication
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/item", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"" + ID_LABEL + "\": \"itemId999\",\"" + ITEM_NAME_LABEL + "\": \"itemName999\",\""
						+ ITEM_CATEGORY_LABEL + "\" : \"itemCategory999\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized()).andDo(print());
	}

}
