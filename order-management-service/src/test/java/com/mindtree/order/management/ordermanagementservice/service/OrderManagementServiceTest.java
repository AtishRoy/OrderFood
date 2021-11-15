package com.mindtree.order.management.ordermanagementservice.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindtree.order.management.ordermanagementservice.controller.CustomerManagementProxy;
import com.mindtree.order.management.ordermanagementservice.controller.impl.CustomerManagementFallback;
import com.mindtree.order.management.ordermanagementservice.dto.CustomerIdResponse;
import com.mindtree.order.management.ordermanagementservice.exception.ItemsException;
import com.mindtree.order.management.ordermanagementservice.exception.OrderNotFoundException;
import com.mindtree.order.management.ordermanagementservice.model.Item;
import com.mindtree.order.management.ordermanagementservice.model.Order;
import com.mindtree.order.management.ordermanagementservice.model.OrderStatusType;
import com.mindtree.order.management.ordermanagementservice.model.PaymentMode;
import com.mindtree.order.management.ordermanagementservice.model.PaymentStatusType;
import com.mindtree.order.management.ordermanagementservice.model.Transaction;
import com.mindtree.order.management.ordermanagementservice.repository.OrderManagementRepository;
import com.mindtree.order.management.ordermanagementservice.service.impl.OrderManagementServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderManagementServiceTest {

	@MockBean
	private OrderManagementRepository repositoryMock;
	@Autowired
	private OrderManagementServiceImpl service;
	@Autowired
	private HttpSession session;
	@MockBean(name="customerManagementProxy")
    private CustomerManagementProxy customerManagementProxy;
	@MockBean
	private CustomerManagementFallback customerManagementFallback;

	@Test
	public void testFetchOrder() {
		service.setHazelcastCacheSwitch("FALSE");
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
		List<Item> items = new ArrayList<>();
		items.add(new Item(1234l, "Chapathi", 2));
		items.add(new Item(2345l, "Paneer Masala", 1));
		Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
		Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
		order = service.fetchOrder(1111l);
		Assert.assertNotNull(order);
	}

    @Test
    public void testFetchOrderFromHazelCache() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
            PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction);
        Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
        service.orderMap().put("1111", order);
        service.setHazelcastCacheSwitch("TRUE");
        order = service.fetchOrder(1111l);
        Assert.assertNotNull(order);
    }

/*    @Test
    public void testFetchOrder2() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
            PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction);
        Mockito.when(repositoryMock.findByOrderId(Mockito.anyLong())).thenReturn(order);
        service.setHazelcastCacheSwitch("FALSE");
        order = service.fetchOrder(1111l);
        Assert.assertNotNull(order);
    }*/

/*    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrder3() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
            PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction);
        Mockito.when(repositoryMock.findByOrderId(Mockito.anyLong())).thenReturn(null);
        service.setHazelcastCacheSwitch("FALSE");
        order = service.fetchOrder(1111l);
        Assert.assertNotNull(order);
    }*/

    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrderForException() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = null;
        service.setHazelcastCacheSwitch("FALSE");
        Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
        order = service.fetchOrder(1111l);
        Assert.assertNotNull(order);
    }

	@Test
	public void testFetchOrderWithNotFoundException() {
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
		List<Item> items = new ArrayList<>();
		items.add(new Item(1234l, "Chapathi", 2));
		items.add(new Item(2345l, "Paneer Masala", 1));
		Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
		Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
		try {
			order = service.fetchOrder(1112l);
		}
		catch (OrderNotFoundException exception) {
			Assert.assertNotNull(exception);
		}
	}

	@Test
	public void testFetchAllOrders() {
		try {
			session.setAttribute("X-ACCESS-TOKEN", "SESSION");
			CustomerIdResponse customerResponse = new CustomerIdResponse();
			customerResponse.setCustomerId("9999");
			ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
					HttpStatus.OK);
			customerManagementProxy = customerManagementFallback;
			Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
			Transaction transaction1 = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
			Transaction transaction2 = new Transaction(2121l, "4DDA8FF597GTEEA2", PaymentMode.COD, PaymentStatusType.SUCCESS);
			List<Item> items1 = new ArrayList<>();
			items1.add(new Item(1234l, "Chapathi", 2));
			items1.add(new Item(2345l, "Paneer Masala", 1));
			List<Item> items2 = new ArrayList<>();
			items2.add(new Item(3456l, "Fried Rice", 1));
			List<Order> orders = new ArrayList<>();
			orders.add(new Order(1111L, 8689283L, "8888", 9999L, items1, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction1));
			orders.add(new Order(2222L, 8680987L, "4444", 5555L, items2, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction2));
			Mockito.when(repositoryMock.findByCustomerId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
			orders = service.fetchAllOrders(0, 1);
			Assert.assertNotNull(orders);
			//Assert.assertEquals(8689283L, orders.get(0).getOrderId().longValue());
			Assert.assertNotNull(orders.get(0));
		}
		catch (Exception ex) {
			Assert.fail();
		}
	}

    @Test
    public void testFetchAllOrdersFromHazelCache() {
    	session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction1 = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
            PaymentStatusType.SUCCESS);
        Transaction transaction2 = new Transaction(2121l, "4DDA8FF597GTEEA2", PaymentMode.COD,
            PaymentStatusType.SUCCESS);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(1234l, "Chapathi", 2));
        items1.add(new Item(2345l, "Paneer Masala", 1));
        List<Item> items2 = new ArrayList<>();
        items2.add(new Item(3456l, "Fried Rice", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items1, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction1));
        orders.add(new Order(2222L, 8680987L, "4444", 5555L, items2, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction2));
        service.setHazelcastCacheSwitch("TRUE");
        service.orderCustMap().put("9999", orders);
        orders = service.fetchAllOrders(null, null);
        Assert.assertNotNull(orders.get(0));
    }

    @Test
    public void testFetchAllOrdersWithPagination() {
    	session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction1 = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
            PaymentStatusType.SUCCESS);
        Transaction transaction2 = new Transaction(2121l, "4DDA8FF597GTEEA2", PaymentMode.COD,
            PaymentStatusType.SUCCESS);
        List<Item> items1 = new ArrayList<>();
        items1.add(new Item(1234l, "Chapathi", 2));
        items1.add(new Item(2345l, "Paneer Masala", 1));
        List<Item> items2 = new ArrayList<>();
        items2.add(new Item(3456l, "Fried Rice", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items1, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction1));
        orders.add(new Order(2222L, 8680987L, "4444", 5555L, items2, 100F, "Address1", "9087654321",
            OrderStatusType.PLACED, transaction2));
        Mockito.when(repositoryMock.findByCustomerId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        service.orderCustMap().put("9999", orders);
        orders = service.fetchAllOrders(0, 2);
        Assert.assertNotNull(orders.get(0));
    }

    @Test(expected=OrderNotFoundException.class)
    public void testFetchAllOrdersWithException() {
        Mockito.when(repositoryMock.findByCustomerId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class))).thenReturn(null);
        service.setHazelcastCacheSwitch("FALSE");
        service.fetchAllOrders(0, 1);
    }

    @Test(expected=OrderNotFoundException.class)
    public void testFetchAllOrdersWithOrderNotFoundException() {
        List<Order> orders = new ArrayList<>();
        Mockito.when(repositoryMock.findByCustomerId(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        orders = service.fetchAllOrders(0, 1);
    }

	@Test
	public void testPlaceOrder() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
		List<Item> items = new ArrayList<>();
		items.add(new Item(1234l, "Chapathi", 2));
		items.add(new Item(2345l, "Paneer Masala", 1));
		Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
		Mockito.when(repositoryMock.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
        service.orderCustMap().remove("9999");
        service.orderRestMap().remove("8888");
        service.orderCustRestMap().remove("8888CR9999");
		order = service.placeOrder(order);
		Assert.assertNotNull(order);
		service.orderCustMap().remove("9999");
		service.orderRestMap().remove("8888");
		service.orderCustRestMap().remove("8888CR9999");
	}

/*	@Test
    public void testPlaceOrder1() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<CustomerIdResponse>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(Mockito.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        Mockito.when(repositoryMock.save(Mockito.any(Order.class))).thenReturn(order);
        service.orderCustMap().put("9999", new ArrayList<Order>());
        service.orderRestMap().put("8888", new ArrayList<Order>());
        service.orderCustRestMap().put("8888CR9999", new ArrayList<Order>());
        order = service.placeOrder(order);
        Assert.assertNotNull(order);
        service.orderCustMap().remove("9999");
		service.orderRestMap().remove("8888");
		service.orderCustRestMap().remove("8888CR9999");
    }*/

    @Test(expected=ItemsException.class)
    public void testPlaceOrderForItemException() {
    	session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 999));
        items.add(new Item(2345l, "Paneer Masala", 10));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        order = service.placeOrder(order);
    }

	@Test(expected=ItemsException.class)
    public void testPlaceOrderItemsException() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(2345l, "Paneer Masala", 10));
        items.add(new Item(1234l, "Chapathi", 99));
        items.add(new Item(2345l, "Paneer Masala", 10));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        order = service.placeOrder(order);
    }

	@Test(expected=ItemsException.class)
    public void testPlaceOrderForException() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(2345l, "a", 1));
        items.add(new Item(2345l, "b", 1));
        items.add(new Item(1234l, "c", 1));
        items.add(new Item(2345l, "d", 1));
        items.add(new Item(1234l, "e", 1));
        items.add(new Item(2345l, "f", 1));
        items.add(new Item(1234l, "g", 1));
        items.add(new Item(2345l, "h", 1));
        items.add(new Item(1234l, "i", 1));
        items.add(new Item(2345l, "j", 1));
        items.add(new Item(2345l, "k", 1));
        items.add(new Item(1234l, "l", 1));
        items.add(new Item(2345l, "m", 1));
        items.add(new Item(1234l, "n", 1));
        items.add(new Item(1234l, "o", 1));
        items.add(new Item(2345l, "p", 1));
        items.add(new Item(1234l, "q", 1));
        items.add(new Item(2345l, "r", 1));
        items.add(new Item(1234l, "s", 1));
        items.add(new Item(2345l, "t", 1));
        items.add(new Item(1234l, "u", 1));
        items.add(new Item(2345l, "v", 1));
        items.add(new Item(1234l, "w", 1));
        items.add(new Item(2345l, "x", 1));
        items.add(new Item(1234l, "y", 1));
        items.add(new Item(2345l, "z", 1));
        items.add(new Item(2345l, "aa", 1));
        items.add(new Item(2345l, "ab", 1));
        items.add(new Item(1234l, "ac", 1));
        items.add(new Item(2345l, "ad", 1));
        items.add(new Item(1234l, "ae", 1));
        items.add(new Item(2345l, "af", 1));
        items.add(new Item(1234l, "ag", 1));
        items.add(new Item(2345l, "ah", 1));
        items.add(new Item(1234l, "ai", 1));
        items.add(new Item(2345l, "aj", 1));
        items.add(new Item(2345l, "ak", 1));
        items.add(new Item(1234l, "al", 1));
        items.add(new Item(2345l, "am", 1));
        items.add(new Item(1234l, "an", 1));
        items.add(new Item(1234l, "ao", 1));
        items.add(new Item(2345l, "ap", 1));
        items.add(new Item(1234l, "aq", 1));
        items.add(new Item(2345l, "ar", 1));
        items.add(new Item(1234l, "as", 1));
        items.add(new Item(2345l, "at", 1));
        items.add(new Item(1234l, "au", 1));
        items.add(new Item(2345l, "av", 1));
        items.add(new Item(1234l, "aw", 1));
        items.add(new Item(2345l, "ax", 1));
        items.add(new Item(1234l, "ay", 1));
        items.add(new Item(2345l, "az", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        order = service.placeOrder(order);
    }

	@Test
	public void testUpdateOrder() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD,
				PaymentStatusType.SUCCESS);
		List<Item> items = new ArrayList<>();
		items.add(new Item(1234l, "Chapathi", 2));
		items.add(new Item(2345l, "Paneer Masala", 1));
		Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321",
				OrderStatusType.PLACED, transaction);
		List<Order> orders = new ArrayList<>();
		orders.add(order);
		List<Order> orderList = new ArrayList<>();
		orderList.add(order);
		service.orderCustMap().put("9999", orderList);
		service.orderRestMap().put("8888", orderList);
		service.orderCustRestMap().put("8888CR9999", orderList);
		Mockito.when(repositoryMock.findByOrderIdAndRestaurantId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
				.thenReturn(orders);
		Mockito.when(repositoryMock.deleteByOrderId(ArgumentMatchers.anyLong())).thenReturn(1);
		Mockito.when(repositoryMock.save(ArgumentMatchers.any(Order.class))).thenReturn(order);
		order = service.updateOrder(order);
		Assert.assertEquals(8689283L, order.getOrderId().longValue());
		service.orderCustMap().remove("9999");
		service.orderRestMap().remove("8888");
		service.orderCustRestMap().remove("8888CR9999");
	}

	@Test(expected=OrderNotFoundException.class)
    public void testUpdateOrderWithException() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, null, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        order = service.updateOrder(order);
    }

	@Test(expected=OrderNotFoundException.class)
    public void testUpdateOrderWhenOrderIsNull() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        List<Order> orders = new ArrayList<>();
        Mockito.when(repositoryMock.findByOrderIdAndRestaurantId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(orders);
        order = service.updateOrder(order);
    }
	@Test(expected=OrderNotFoundException.class)
    public void testUpdateOrderWhenOrderNotFound() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        Mockito.when(repositoryMock.findByOrderIdAndRestaurantId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(null);
        order = service.updateOrder(order);
    }
	@Test(expected=OrderNotFoundException.class)
    public void testUpdateOrderWhenOrderStatusCancelled() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.CANCELLED, transaction);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        Mockito.when(repositoryMock.findByOrderIdAndRestaurantId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(orders);
        order = service.updateOrder(order);
    }

	@Test(expected=OrderNotFoundException.class)
    public void testUpdateOrderWhenException() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        Mockito.when(repositoryMock.findByOrderIdAndRestaurantId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString())).thenReturn(orders);
        Mockito.when(repositoryMock.deleteByOrderId(ArgumentMatchers.anyLong())).thenReturn(0);
        order = service.updateOrder(order);
    }

	@Test
	public void testFetchOrderWithRestuarantID() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
		List<Item> items = new ArrayList<>();
		items.add(new Item(1234l, "Chapathi", 2));
		items.add(new Item(2345l, "Paneer Masala", 1));
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
		Mockito.when(repositoryMock.findByRestaurantIdAndCustomerId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(orders);
		List<Order> fetchedorders = service.fetchOrdersOfCustomerByRestaurantId("8888");
		Assert.assertNotNull(fetchedorders.get(0));
	}

    @Test
    public void testFetchOrderWithRestuarantIDFromHazelCache() {
    	session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
        service.setHazelcastCacheSwitch("TRUE");
        service.orderCustRestMap().put("8888CR9999", orders);
        List<Order> fetchedorders = service.fetchOrdersOfCustomerByRestaurantId("8888");
        Assert.assertNotNull(fetchedorders.get(0));
    }

   /* @Test
    public void testFetchOrderWithRestuarantID2() {
    	session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<CustomerIdResponse>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(Mockito.anyString())).thenReturn(response);
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        List<Order> orders = new ArrayList<Order>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
        Mockito.when(repositoryMock.findByRestaurantIdAndCustomerId(Mockito.anyString(), Mockito.anyLong())).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        List<Order> fetchedorders = service.fetchOrdersOfCustomerByRestaurantId("8888");
        Assert.assertNotNull(fetchedorders.get(0));
    }*/

    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrderWithRestuarantIDWithEmptyOrder() {
        Mockito.when(repositoryMock.findByRestaurantIdAndCustomerId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(null);
        service.setHazelcastCacheSwitch("FALSE");
        service.fetchOrdersOfCustomerByRestaurantId("8888");
    }

    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrderWithRestuarantIDWhenOrderIsNull() {
        List<Order> orders = new ArrayList<>();
        Mockito.when(repositoryMock.findByRestaurantIdAndCustomerId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        service.fetchOrdersOfCustomerByRestaurantId("8888");
    }

	@Test
	public void testFetchOrderWithRestuarantIDNotFoundException() {
		session.setAttribute("X-ACCESS-TOKEN", "SESSION");
		CustomerIdResponse customerResponse = new CustomerIdResponse();
		customerResponse.setCustomerId("9999");
		ResponseEntity<CustomerIdResponse> response = new ResponseEntity<>(customerResponse,
				HttpStatus.OK);
		customerManagementProxy = customerManagementFallback;
		Mockito.when(customerManagementFallback.getCustomerWithId(ArgumentMatchers.anyString())).thenReturn(response);
		Mockito.when(repositoryMock.findByRestaurantId(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(null);
		try {
			service.fetchOrdersOfCustomerByRestaurantId("8888");
		}
		catch (OrderNotFoundException exception) {
			Assert.assertEquals("The requested order is not found", exception.getMessage());
		}

	}

	@Test
	public void testCancelOrder() {
		int rowsAffected = 1;
		Mockito.when(repositoryMock.cancelOrderById(ArgumentMatchers.anyLong(),
				ArgumentMatchers.any(OrderStatusType.class))).thenReturn(rowsAffected);
		Order order = new Order();
		order.setOrderStatus(OrderStatusType.CANCELLED);
		Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
		service.cancelOrder(6);
	}

	@Test(expected=OrderNotFoundException.class)
    public void testCancelOrderWithException() {
        int rowsAffected = 0;
        Mockito.when(repositoryMock.cancelOrderById(ArgumentMatchers.anyLong(),
                ArgumentMatchers.any(OrderStatusType.class))).thenReturn(rowsAffected);
        Order order = new Order();
        order.setOrderStatus(OrderStatusType.DELIVERED);
        Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
        service.cancelOrder(6);
    }

	@Test
	public void testCancelOrderWithNotFoundException() {
		Mockito.when(repositoryMock.cancelOrderById(ArgumentMatchers.anyLong(),
				ArgumentMatchers.any(OrderStatusType.class))).thenReturn(0);
		Order order = new Order();
		order.setOrderStatus(OrderStatusType.CANCELLED);
		Mockito.when(repositoryMock.findByOrderId(ArgumentMatchers.anyLong())).thenReturn(order);
		try {
			service.cancelOrder(6);
		}
		catch (OrderNotFoundException exception) {
			Assert.assertEquals("Could not cancel the order as order was not found", exception.getMessage());

		}
	}

	@Test
	public void testFetchOrderByRestId() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
        Mockito.when(repositoryMock.findByRestaurantId(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
        List<Order> fetchedorders = service.fetchOrdersByRestaurantId("8888", 0, 1);
        Assert.assertNotNull(fetchedorders.get(0));
    }

    @Test
    public void testFetchOrderByRestIdForHazelCaste() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
        service.setHazelcastCacheSwitch("TRUE");
        service.orderRestMap().put("8888", orders);
        List<Order> fetchedorders = service.fetchOrdersByRestaurantId("8888", null, null);
        Assert.assertNotNull(fetchedorders.get(0));
    }
    @Test
    public void testFetchOrderByRestWithPagination() {
        Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
        List<Item> items = new ArrayList<>();
        items.add(new Item(1234l, "Chapathi", 2));
        items.add(new Item(2345l, "Paneer Masala", 1));
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction));
        Mockito.when(repositoryMock.findByRestaurantId(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        service.orderRestMap().put("8888", orders);
        List<Order> fetchedorders = service.fetchOrdersByRestaurantId("8888", 0, 1);
        Assert.assertNotNull(fetchedorders.get(0));
    }

    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrderByRestIdWithException() {
        Mockito.when(repositoryMock.findByRestaurantId(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(null);
        service.setHazelcastCacheSwitch("FALSE");
        service.fetchOrdersByRestaurantId("8888",0,1);
    }

    @Test(expected=OrderNotFoundException.class)
    public void testFetchOrderByRestIdWithExceptionAndPagination() {
        List<Order> orders = new ArrayList<>();
        Mockito.when(repositoryMock.findByRestaurantId(ArgumentMatchers.anyString(), ArgumentMatchers.any(Pageable.class))).thenReturn(orders);
        service.setHazelcastCacheSwitch("FALSE");
        service.fetchOrdersByRestaurantId("8888", 0, 1);
    }
}
