package com.mindtree.order.management.ordermanagementservice.respository;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindtree.order.management.ordermanagementservice.model.Item;
import com.mindtree.order.management.ordermanagementservice.model.Order;
import com.mindtree.order.management.ordermanagementservice.model.OrderStatusType;
import com.mindtree.order.management.ordermanagementservice.model.PaymentMode;
import com.mindtree.order.management.ordermanagementservice.model.PaymentStatusType;
import com.mindtree.order.management.ordermanagementservice.model.Transaction;
import com.mindtree.order.management.ordermanagementservice.repository.OrderManagementRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderManagementRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OrderManagementRepository repository;

	@Before
	public void setup() {
		entityManager.clear();
	}

	@After
	public void remove() {
		entityManager.clear();
	}

	@Test
	public void testFetchAllOrdersWithNotFound() {
		List<Order> orders = repository.findByCustomerId(9999L, null);
		Assert.assertTrue(orders.isEmpty());
	}

	@Test
	public void testFetchAllOrders() {
		List<Item> items = new ArrayList<>();
		Item item = new Item(1234l, "Chapathi", 2);
		items.add(item);
		Transaction transaction = new Transaction(1212l, "3FE4DDA55F99EEA2", PaymentMode.CARD, PaymentStatusType.SUCCESS);
		Order order = new Order(1111L, 8689283L, "8888", 9999L, items, 100F, "Address1", "9087654321", OrderStatusType.PLACED, transaction);
		entityManager.merge(order);
		entityManager.flush();
		List<Item> items1 = new ArrayList<>();
		Item item1 = new Item(4321l, "Roti", 5);
		Item item2 = new Item(9876l, "Curry", 1);
		items1.add(item1);
		items1.add(item2);
		Transaction transaction1 = new Transaction(2121l, "2AEE99F55ADD4EF3", PaymentMode.COD, PaymentStatusType.FAILURE);
		Order order1 = new Order(2222L, 3829868L, "4444", 9999L, items1, 250F, "Address2", "9123456780", OrderStatusType.CANCELLED, transaction1);
		entityManager.merge(order1);
		entityManager.flush();
		List<Item> items2 = new ArrayList<>();
		Item item3 = new Item(1234l, "Chapathi", 2);
		items2.add(item3);
		Transaction transaction2 = new Transaction(2323l, "3EF4BBA55F99EEA2", PaymentMode.UPI, PaymentStatusType.SUCCESS);
		Order order2 = new Order(3333L, 8765183L, "7777", 5555L, items2, 50F, "Address3", "9345628190", OrderStatusType.UPDATED, transaction2);
		entityManager.merge(order2);
		entityManager.flush();
		List<Order> orders = repository.findByCustomerId(9999L, null);
		Assert.assertFalse(orders.isEmpty());
		Assert.assertEquals(2, orders.size());
		Assert.assertEquals(8689283L, orders.get(0).getOrderId().longValue());
		Assert.assertEquals("8888", orders.get(0).getRestaurantId());
		Assert.assertEquals(9999L, orders.get(0).getCustomerId().longValue());
		Assert.assertEquals(100, orders.get(0).getTotalPrice().longValue());
		Assert.assertEquals("Address1", orders.get(0).getDeliveryAddress());
		Assert.assertEquals("9087654321", orders.get(0).getContactNumber());
		Assert.assertEquals("Order is placed", orders.get(0).getOrderStatus().getValue());
		Assert.assertNotNull(orders.get(0).getTransaction());
		Assert.assertEquals("3FE4DDA55F99EEA2", orders.get(0).getTransaction().getTransactionId());
		Assert.assertEquals("Payment Card", orders.get(0).getTransaction().getPaymentMode().getValue());
		Assert.assertEquals("Payment Success", orders.get(0).getTransaction().getPaymentStatus().getValue());
		Assert.assertNotNull(orders.get(0).getItemList());
		Assert.assertFalse(orders.get(0).getItemList().isEmpty());
		Assert.assertEquals(1, orders.get(0).getItemList().size());
		Assert.assertEquals("Chapathi", orders.get(0).getItemList().get(0).getItemName());
		Assert.assertEquals(2, orders.get(0).getItemList().get(0).getQuantity().intValue());
		Assert.assertEquals(3829868L, orders.get(1).getOrderId().longValue());
		Assert.assertEquals("4444", orders.get(1).getRestaurantId());
		Assert.assertEquals(9999L, orders.get(1).getCustomerId().longValue());
		Assert.assertEquals(250, orders.get(1).getTotalPrice().longValue());
		Assert.assertEquals("Address2", orders.get(1).getDeliveryAddress());
		Assert.assertEquals("9123456780", orders.get(1).getContactNumber());
		Assert.assertEquals("Order is cancelled", orders.get(1).getOrderStatus().getValue());
		Assert.assertNotNull(orders.get(1).getTransaction());
		Assert.assertEquals("2AEE99F55ADD4EF3", orders.get(1).getTransaction().getTransactionId());
		Assert.assertEquals("Cash On Delivery", orders.get(1).getTransaction().getPaymentMode().getValue());
		Assert.assertEquals("Payment Failure", orders.get(1).getTransaction().getPaymentStatus().getValue());
		Assert.assertNotNull(orders.get(1).getItemList());
		Assert.assertFalse(orders.get(1).getItemList().isEmpty());
		Assert.assertEquals(2, orders.get(1).getItemList().size());
		Assert.assertEquals("Roti", orders.get(1).getItemList().get(0).getItemName());
		Assert.assertEquals(5, orders.get(1).getItemList().get(0).getQuantity().intValue());
		Assert.assertEquals("Curry", orders.get(1).getItemList().get(1).getItemName());
		Assert.assertEquals(1, orders.get(1).getItemList().get(1).getQuantity().intValue());
	}

	@Test
	public void testFetchOrderWithNotFound() {
		Order order = repository.findByOrderId(8689283L);
		Assert.assertNull(order);
	}

}