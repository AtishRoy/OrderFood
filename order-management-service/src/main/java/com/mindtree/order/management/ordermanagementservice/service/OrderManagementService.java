package com.mindtree.order.management.ordermanagementservice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.mindtree.order.management.ordermanagementservice.model.Order;

@Service
@RefreshScope
public interface OrderManagementService {

	public Order fetchOrder(long id);

	public List<Order> fetchAllOrders(Integer pageNumber, Integer count);

	public List<Order> fetchOrdersOfCustomerByRestaurantId(String id);

	public List<Order> fetchOrdersByRestaurantId(String id, Integer pageNumber, Integer count);

	@Transactional
	public Order placeOrder(Order order);

	@Transactional
	public Order updateOrder(Order order);

	public String cancelOrder(long id);
}
