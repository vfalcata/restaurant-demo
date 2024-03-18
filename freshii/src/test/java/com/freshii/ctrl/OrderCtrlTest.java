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
import org.hamcrest.Matchers;
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
import com.freshii.ctrl.fixtures.MockCookieRequest;
import com.freshii.dao.CustomerService;
import com.freshii.dao.ItemService;
import com.freshii.dao.ManagerService;
import com.freshii.dao.OrderService;
import com.freshii.model.Customer;
import com.freshii.model.Item;
import com.freshii.model.Manager;
import com.freshii.model.Order;
import com.freshii.web.ctrl.OrderCtrl;

@WebMvcTest(controllers = OrderCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderCtrlTest {

	private static final String MANAGER_ROLE = "MANAGER";
	private static final String CUSTOMER_ROLE = "CUSTOMER";
	private static final String ID_LABEL = "id";
	private static final String CUSTOMER_ORDER_ID_LABEL = "customerId";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String ROLE_LABEL = "role";
	private static final String ORDERSTATUS_LABEL = "orderStatus";
	private static final String ORDERITEMS_LABEL = "items";
	private static final String ITEM_NAME_LABEL = "name";
	private static final String ITEM_CATEGORY_LABEL = "category";
	private static final String CANCELLED_ORDER_STATUS = "CANCELLED";
	private static final String DELIVERING_ORDER_STATUS = "DELIVERING";
	private static final String PAID_ORDER_STATUS = "PAID";
	private static final String UNPAID_ORDER_STATUS = "UNPAID";
	private static final String SIGNED_ORDER_STATUS = "SIGNED";

	@MockBean
	private ManagerService mockManagerService;
	@MockBean
	private OrderService mockOrderService;
	@MockBean
	private ItemService mockItemService;
	@MockBean
	private CustomerService mockCustomerService;

	@Autowired
	private MockMvc mockMvc;

	private List<Item> items;
	private List<Manager> managers;
	private List<Customer> customers;
	private List<Order> orders;
	private Manager manager;

	@BeforeEach
	public void setup() {
		this.items = new ArrayList<>();
		items.add(new Item.Builder().id("itemid1").name("itemname1").category("itemcategory1").build());
		items.add(new Item.Builder().id("itemid2").name("itemname2").category("itemcategory2").build());
		items.add(new Item.Builder().id("itemid3").name("itemname3").category("itemcategory3").build());
		items.add(new Item.Builder().id("itemid4").name("itemname4").category("itemcategory4").build());
		items.add(new Item.Builder().id("itemid5").name("itemname5").category("itemcategory5").build());
		items.add(new Item.Builder().id("itemid6").name("itemname6").category("itemcategory6").build());

		this.manager = new Manager.Builder("mockManager1", "mockpass1").id("123").build();
		this.managers = new ArrayList<>();
		managers.add(new Manager.Builder("mockManager1", "mockpass1").id("123").build());
		managers.add(new Manager.Builder("mockManager2", "mockpass2").id("456").build());
		managers.add(new Manager.Builder("mockManager3", "mockpass3").id("789").build());
		managers.add(new Manager.Builder("mockManager4", "mockpass4").id("987").build());
		managers.add(new Manager.Builder("mockManager5", "mockpass5").id("654").build());

		this.customers = new ArrayList<>();
		customers.add(new Customer.Builder("cust1", "custpass1").id("cuid1").build());
		customers.add(new Customer.Builder("cust2", "custpass2").id("cuid2").build());
		customers.add(new Customer.Builder("cust3", "custpass3").id("cuid3").build());
		customers.add(new Customer.Builder("cust4", "custpass4").id("cuid4").build());

		this.orders = new ArrayList<>();
		Map<String, Item> orderItems1 = new HashMap<String, Item>();
		Item changedItem1 = this.items.get(0);
		changedItem1.setSize("medium");
		changedItem1.setQuantity(2);
		Item changedItem2 = this.items.get(1);
		changedItem2.setSize("small");
		changedItem2.setQuantity(1);
		Item changedItem3 = this.items.get(2);
		changedItem3.setSize("large");
		changedItem3.setQuantity(4);
		orderItems1.put(this.items.get(0).getId(), changedItem1);
		orderItems1.put(this.items.get(1).getId(), changedItem2);
		orderItems1.put(this.items.get(2).getId(), changedItem3);
		this.orders.add(new Order.Builder("cuid1").id("oid1").items(orderItems1).orderStatus("UNPAID").build());

		Map<String, Item> orderItems2 = new HashMap<String, Item>();
		Item changedItem4 = this.items.get(3);
		changedItem4.setSize("small");
		changedItem4.setQuantity(33);
		Item changedItem5 = this.items.get(4);
		changedItem5.setSize("large");
		changedItem5.setQuantity(9);
		Item changedItem6 = this.items.get(5);
		changedItem6.setSize("large");
		changedItem6.setQuantity(12);
		orderItems2.put(this.items.get(3).getId(), changedItem4);
		orderItems2.put(this.items.get(4).getId(), changedItem5);
		orderItems2.put(this.items.get(5).getId(), changedItem6);
		this.orders.add(new Order.Builder("cuid3").id("oid2").items(orderItems2).orderStatus("UNPAID").build());
	}

	@Test
	@DisplayName("Should get order by Id")
	public void shouldGetOrderById() throws Exception {
		// manager gets
		Mockito.when(mockOrderService.getOrder("oid1")).thenReturn(orders.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/oid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + CUSTOMER_ORDER_ID_LABEL).value("cuid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value("UNPAID"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.size").value("medium"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.quantity").value("2"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.size").value("small"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.quantity").value("1"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.quantity").value("4"))
				.andExpect(status().is(200)).andDo(print());

		Mockito.when(mockOrderService.getOrder("oid2")).thenReturn(orders.get(1));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/oid2", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid2"))
				.andExpect(jsonPath("$." + CUSTOMER_ORDER_ID_LABEL).value("cuid3"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value("UNPAID"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid4.size").value("small"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid4.quantity").value("33"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid5.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid5.quantity").value("9"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid6.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid6.quantity").value("12"))
				.andExpect(status().is(200)).andDo(print());

		// customer gets
		Mockito.when(mockOrderService.getOrder("oid1")).thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/oid1", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + CUSTOMER_ORDER_ID_LABEL).value("cuid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value("UNPAID"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.size").value("medium"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.quantity").value("2"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.size").value("small"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.quantity").value("1"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.quantity").value("4"))
				.andExpect(status().is(200)).andDo(print());

		Mockito.when(mockOrderService.getOrder("oid2")).thenReturn(orders.get(1));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/oid2", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid2"))
				.andExpect(jsonPath("$." + CUSTOMER_ORDER_ID_LABEL).value("cuid3"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value("UNPAID"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid4.size").value("small"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid4.quantity").value("33"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid5.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid5.quantity").value("9"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid6.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid6.quantity").value("12"))
				.andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not get order")
	public void shouldNotGetOrderById() throws Exception {
		// order not exists
		Mockito.when(mockOrderService.getOrder("oid1")).thenReturn(null);
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/oid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication for manager
		Mockito.when(mockOrderService.getOrder("oid1")).thenReturn(orders.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidGetRequest("/order/oid1", manager.getUsername(), manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());

		// customer does not own order
		Mockito.when(mockOrderService.getOrder("oid1")).thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(1).getUsername(),
				this.customers.get(1).getPassword())).thenReturn(this.customers.get(1));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidGetRequest("/order/oid1", this.customers.get(1).getUsername(),
						this.customers.get(1).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should delete item from order")
	public void shouldDeleteItemFromOrder() throws Exception {
		Mockito.when(mockOrderService.deleteOrderItem(this.customers.get(0).getId(), "itemid1"))
				.thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/delete/item/itemid1", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1")).andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not delete item from order")
	public void shouldNotDeleteItem() throws Exception {
		// item does not exist
		Mockito.when(mockOrderService.deleteOrderItem(this.customers.get(0).getId(), "itemid1")).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/delete/item/itemid1", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/delete/item/itemid1", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should update item from order")
	public void shouldUpdateItemFromOrder() throws Exception {
		Mockito.when(mockOrderService.updateOrder(this.customers.get(0).getId(), "itemid1", "large", 4))
				.thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/large/4", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1")).andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not update item from order")
	public void shouldNotUpdateItemFromOrder() throws Exception {
		// item does not exist
		Mockito.when(mockOrderService.updateOrder(this.customers.get(0).getId(), "itemid1", "large", 4))
				.thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/large/4", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/update/itemid1/large/4", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should update item size from order")
	public void shouldUpdateItemSizeFromOrder() throws Exception {
		Mockito.when(mockOrderService.updateOrderItemSize(this.customers.get(0).getId(), "itemid1", "large"))
				.thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/size/large", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1")).andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not update item size from order")
	public void shouldNotUpdateItemSizeFromOrder() throws Exception {
		// item does not exist
		Mockito.when(mockOrderService.updateOrderItemSize(this.customers.get(0).getId(), "itemid1", "large"))
				.thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/size/large", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/update/itemid1/size/large", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should update item quantity from order")
	public void shouldUpdateItemQuantityFromOrder() throws Exception {
		Mockito.when(mockOrderService.updateOrderItemQuantity(this.customers.get(0).getId(), "itemid1", 7))
				.thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/quantity/7", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1")).andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not update item quantity from order")
	public void shouldNotUpdateItemQuantityFromOrder() throws Exception {
		// item does not exist
		Mockito.when(mockOrderService.updateOrderItemQuantity(this.customers.get(0).getId(), "itemid1", 7))
				.thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/update/itemid1/quantity/7", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/update/itemid1/quantity/7", this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should cancel order")
	public void shouldCancelOrder() throws Exception {
		this.orders.get(0).setOrderStatusPaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value(CANCELLED_ORDER_STATUS)).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should not cancel order")
	public void shouldNotCancelOrder() throws Exception {
		// invalid status to cancel
		this.orders.get(0).setOrderStatusDelivering();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// invalid status to cancel
		this.orders.get(0).setOrderStatusSigned();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// invalid status to cancel
		this.orders.get(0).setOrderStatusUnpaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// order does not exist
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(null);
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// order did not save
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// poor authentication
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/cancel/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should process unpaid order")
	public void shouldSetOrderToPaid() throws Exception {
		this.orders.get(0).setOrderStatusUnpaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value(PAID_ORDER_STATUS)).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should process paid order")
	public void shouldSetOrderToDelivering() throws Exception {
		this.orders.get(0).setOrderStatusPaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(manager);
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(), this.manager.getUsername(),
						this.manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value(DELIVERING_ORDER_STATUS))
				.andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should process signed order")
	public void shouldSetOrderToSigned() throws Exception {
		this.orders.get(0).setOrderStatusDelivering();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value(SIGNED_ORDER_STATUS)).andExpect(status().is(200))
				.andDo(print());
	}

	@Test
	@DisplayName("Should process order at any stage")
	public void shouldNotProcessOrder() throws Exception {
		// order failed to save
		this.orders.get(0).setOrderStatusDelivering();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(null);
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

		// order does not exist
		this.orders.get(0).setOrderStatusDelivering();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication for manager process status
		this.orders.get(0).setOrderStatusPaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockManagerService.loginManager(manager.getUsername(), manager.getPassword())).thenReturn(null);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidPatchRequest("/order/cancel/" + this.orders.get(0).getId(), this.manager.getUsername(),
						this.manager.getPassword(), MANAGER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());

		// poor authentication for customer process status
		this.orders.get(0).setOrderStatusUnpaid();
		Mockito.when(mockOrderService.saveOrder(this.orders.get(0))).thenReturn(this.orders.get(0));
		Mockito.when(mockOrderService.getOrder(this.orders.get(0).getId())).thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(null);
		mockMvc.perform(MockCookieRequest
				.cookiePatchRequest("/order/nextstep/" + this.orders.get(0).getId(),
						this.customers.get(0).getUsername(), this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should get orders")
	public void shouldGetOrders() throws Exception {
		Mockito.when(mockOrderService.getOrders(this.customers.get(0).getId())).thenReturn(this.orders);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/orders", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.*", Matchers.isA(ArrayList.class)))
				.andExpect(jsonPath("$.*", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[*]." + ID_LABEL, Matchers.containsInAnyOrder("oid1", "oid2")))
				.andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not get orders")
	public void shouldNotGetOrders() throws Exception {
		// no orders
		Mockito.when(mockOrderService.getOrders(this.customers.get(0).getId())).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/orders", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// poor authentication for customer process
		Mockito.when(mockOrderService.getOrders(this.customers.get(0).getId())).thenReturn(this.orders);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(null);
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/orders", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

	@Test
	@DisplayName("Should get customer only unpaid order")
	public void shouldGetCustomerOnlyUnpaidOrder() throws Exception {
		Mockito.when(mockOrderService.getCustomerUnpaidOrder(this.customers.get(0).getId()))
				.thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/order", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1")).andExpect(status().is(200)).andDo(print());
	}

	@Test
	@DisplayName("Should not get customer only unpaid order")
	public void shouldNotGetCustomerOnlyUnpaidOrder() throws Exception {

		// no unpaid orders
		Mockito.when(mockOrderService.getCustomerUnpaidOrder(this.customers.get(0).getId())).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/order", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());

		// unauthorized
		Mockito.when(mockOrderService.getCustomerUnpaidOrder(this.customers.get(0).getId()))
				.thenReturn(this.orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(null);
		mockMvc.perform(
				MockCookieRequest
						.cookieGetRequest("/order", this.customers.get(0).getUsername(),
								this.customers.get(0).getPassword(), CUSTOMER_ROLE)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}
	
	@Test
	@DisplayName("Should get order by phone and number")
	public void shouldGetOrderByPhoneAndNumber() throws Exception {
		// manager gets
		int number = 45;
		String phone = "905-111-1111";
		orders.get(0).setNumber(number);
		orders.get(0).setPhone(phone);
		Mockito.when(mockOrderService.getOrderByPhoneAndNumber(phone,number)).thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/phone/"+phone+"/number/"+number, this.customers.get(0).getUsername(),this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$." + ID_LABEL).value("oid1"))
				.andExpect(jsonPath("$." + CUSTOMER_ORDER_ID_LABEL).value("cuid1"))
				.andExpect(jsonPath("$." + ORDERSTATUS_LABEL).value("UNPAID"))
				.andExpect(jsonPath("$.phone").value(phone))
				.andExpect(jsonPath("$.number").value(number))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.size").value("medium"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid1.quantity").value("2"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.size").value("small"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid2.quantity").value("1"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.size").value("large"))
				.andExpect(jsonPath("$." + ORDERITEMS_LABEL + ".itemid3.quantity").value("4"))
				.andExpect(status().is(200)).andDo(print());
	}
	
	@Test
	@DisplayName("Should not get order by phone and number")
	public void shouldNotGetOrderByPhoneAndNumber() throws Exception {
		// order not exists
		int number = 45;
		String phone = "905-111-1111";
		orders.get(0).setNumber(number);
		orders.get(0).setPhone(phone);
		Mockito.when(mockOrderService.getOrderByPhoneAndNumber(phone,number)).thenReturn(null);
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(this.customers.get(0));
		mockMvc.perform(MockCookieRequest
				.cookieGetRequest("/order/phone/"+phone+"/number/"+number, this.customers.get(0).getUsername(),this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());



		// customer does not own order
		Mockito.when(mockOrderService.getOrderByPhoneAndNumber(phone,number)).thenReturn(orders.get(0));
		Mockito.when(mockCustomerService.loginCustomer(this.customers.get(0).getUsername(),
				this.customers.get(0).getPassword())).thenReturn(null);
		mockMvc.perform(MockCookieRequest
				.cookieInvalidGetRequest("/order/phone/"+phone+"/number/"+number, this.customers.get(0).getUsername(),
						this.customers.get(0).getPassword(), CUSTOMER_ROLE)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized()).andDo(print());
	}

}
