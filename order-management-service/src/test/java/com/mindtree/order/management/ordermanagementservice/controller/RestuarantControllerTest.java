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
@WebMvcTest(value = RestuarantController.class, secure = false)
@EnableConfigurationProperties
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class RestuarantControllerTest {
	
	@MockBean
	private OrderManagementService serviceMock;

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testFetchOrderWithRestuarantIDForCustomer() {
		try {
			List<Item> items = new ArrayList<>();
			items.add(new Item(1234l, "Chapathi", 2));
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
			Mockito.when(serviceMock.fetchOrdersOfCustomerByRestaurantId(Mockito.anyString())).thenReturn(orders);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/restuarant").param("restuarantId", "8888")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"[{ \"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"}]",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}

	@Test
	public void testFetchOrderWithRestuarantID() {
		try {
			List<Item> items = new ArrayList<>();
			items.add(new Item(1234l, "Chapathi", 2));
			Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			List<Order> orders = new ArrayList<Order>();
			orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
			Mockito.when(serviceMock.fetchOrdersByRestaurantId(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(orders);
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/restuarant/8888").param("pageNumber", "0").param("count", "10")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(200, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"[{ \"orderId\": 8689283, \"orderStatus\": \"PLACED\", \"transactionId\": \"3FE4DDA55F99EEA2\", \"paymentMode\": \"CARD\", \"paymentStatus\" : \"SUCCESS\"}]",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testFetchOrderWithRestuarantIDNotFoundExceptionForCustomer() {
		try {
			Mockito.when(serviceMock.fetchOrdersOfCustomerByRestaurantId(Mockito.anyString()))
					.thenThrow(new OrderNotFoundException("Restaurant not found"));
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/restuarant").param("restuarantId", "8888")
					.accept(MediaType.APPLICATION_JSON);
			MvcResult result = mockMvc.perform(requestBuilder).andReturn();
			Assert.assertNotNull(result);
			Assert.assertNotNull(result.getResponse());
			Assert.assertEquals(404, result.getResponse().getStatus());
			JSONAssert.assertEquals(
					"{ \"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurant not found\"}",
					result.getResponse().getContentAsString(), false);
		} catch (Exception ex) {
			Assert.fail();
		}
	}
	
	@Test
	public void testFetchOrderWithRestuarantIDNotFoundException() {
		try {
			Mockito.when(serviceMock.fetchOrdersByRestaurantId(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
					.thenThrow(new OrderNotFoundException("The requested order is not found"));
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/restuarant/8888").param("pageNumber", "0").param("count", "10")
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
