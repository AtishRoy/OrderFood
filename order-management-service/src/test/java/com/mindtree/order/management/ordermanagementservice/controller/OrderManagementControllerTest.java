package com.mindtree.order.management.ordermanagementservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.mindtree.order.management.ordermanagementservice.exception.OrderNotFoundException;
import com.mindtree.order.management.ordermanagementservice.model.Item;
import com.mindtree.order.management.ordermanagementservice.model.Order;
import com.mindtree.order.management.ordermanagementservice.model.OrderStatusType;
import com.mindtree.order.management.ordermanagementservice.model.PaymentMode;
import com.mindtree.order.management.ordermanagementservice.model.PaymentStatusType;
import com.mindtree.order.management.ordermanagementservice.model.Transaction;
import com.mindtree.order.management.ordermanagementservice.service.OrderManagementService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderManagementController.class, secure = false)
@EnableConfigurationProperties
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class OrderManagementControllerTest {

	@MockBean
	private OrderManagementService serviceMock;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFetchOrder() {
		try {
			List<Item> items = new ArrayList<>();
			items.add(new Item(1234l, "Chapathi", 2));
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
			Mockito.when(serviceMock.fetchOrder(Mockito.anyLong())).thenReturn(order);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/1111")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testFetchOrderWithNotFoundException() {
		try {
			Mockito.when(serviceMock.fetchOrder(Mockito.anyLong()))
					.thenThrow(new OrderNotFoundException("The requested order is not found"));
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order/1111")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(404, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"The requested order is not found\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testFetchAllOrders() {
		try {
			Transaction transaction1 = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			Transaction transaction2 = new Transaction(2121l, "4DDA8FF597GTEEA2", PaymentMode.COD, PaymentStatusType.SUCCESS);
			List<Item> items1 = new ArrayList<Item>();
			items1.add(new Item(1234l, "Chapathi", 2));
			items1.add(new Item(2345l, "Paneer Masala", 1));
			List<Item> items2 = new ArrayList<Item>();
			items2.add(new Item(3456l, "Fried Rice", 1));
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(1111L, 8689283L, "8888", 9999L, items1, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction1));
			orders.add(new Order(2222L, 8680987L, "4444", 5555L, items2, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction2));
			Mockito.when(serviceMock.fetchAllOrders(Mockito.anyInt(), Mockito.anyInt())).thenReturn(orders);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order").param("pageNumber", "0").param("count", "10").accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"[{ \"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"} , { \"orderId\": 8680987, \"orderStatus\": \"PLACED\", \"transactionId\": 4DDA8FF597GTEEA2, \"paymentMode\": \"COD\", \"paymentStatus\" : \"SUCCESS\"}]",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testFetchAllOrdersWithNotFoundException() {
		try {
			Mockito.when(serviceMock.fetchAllOrders(Mockito.anyInt(), Mockito.anyInt()))
					.thenThrow(new OrderNotFoundException("There are no orders found"));
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/order").param("pageNumber", "0").param("count", "10").accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(404, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"There are no orders found\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testPlaceOrder() {
		try {
			String content = " {\"restaurantId\": 8888,\"customerId\": 9999,\"deliveryAddress\": \"Address1\", \"contactNumber\" : \"9087654321\", \"itemList\": [{\"itemName\": \"Chapathi\",\"quantity\": \"2\"}],\"totalPrice\": \"100\"}";
			List<Item> items = new ArrayList<Item>();
			items.add(new Item(1234l, "Chapathi", 2));
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			Order order = new Order(1111l, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
			Mockito.when(serviceMock.placeOrder(Mockito.any(Order.class))).thenReturn(order);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(201, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{\"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"}\"",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testPlaceOrderWithException() {
		try {
			String content = " {\"restaurantId\": \"\",\"customerId\": null, \"deliveryAddress\": \"Address1\", \"contactNumber\" : \"9087654321\",\"itemList\": [{\"itemName\": \"Chapathi\",\"quantity\": \"2\"}],\"totalPrice\": \"100\"}";
			List<Item> items = new ArrayList<Item>();
			items.add(new Item(1234l, "Chapathi", 2));
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
			Mockito.when(serviceMock.placeOrder(Mockito.any(Order.class))).thenReturn(order);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/order").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(400, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Input data mismatch\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testUpdateOrder() {
		try {
			String content = " {\"orderId\": 1111, \"restaurantId\": 8888,\"customerId\": 9999, \"deliveryAddress\": \"Address1\", \"contactNumber\" : \"9087654321\", \"itemList\": [{\"itemId\": 121, \"itemName\": \"Chapathi\",\"quantity\": \"5\"}],\"totalPrice\": \"100\"}";
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			List<Item> items = new ArrayList<Item>();
			items.add(new Item(1234l, "Chapathi", 2));
			Order order = new Order(1111l, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
			Mockito.when(serviceMock.updateOrder(Mockito.any(Order.class))).thenReturn(order);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/order").accept(MediaType.APPLICATION_JSON)
					.content(content).contentType(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(201, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{\"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"}\"",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testCancelOrder() {
		try {
			Mockito.when(serviceMock.cancelOrder(Mockito.anyLong())).thenReturn("Order cancelled successfully");
			RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/1111")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals("{\"content\": \"Order cancelled successfully\"}", result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testCancelOrderForAlreadyCancelled() {
		try {
			Mockito.when(serviceMock.cancelOrder(Mockito.anyLong())).thenReturn("Order is already cancelled");
			RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/1111")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals("{\"content\": \"Order is already cancelled\"}", result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testCancelOrderWithNotFoundException() {
		try {
			Mockito.when(serviceMock.cancelOrder(Mockito.anyLong())).thenThrow(new OrderNotFoundException("The requested order is not found"));
			RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/order/1111")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(404, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"The requested order is not found\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

}
