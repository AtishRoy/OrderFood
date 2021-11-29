package com.mindtree.order.management.ordermanagementservice.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mindtree.order.management.ordermanagementservice.controller.CustomerManagementProxy;
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
import com.mindtree.order.management.ordermanagementservice.service.OrderManagementService;

@Service
@RefreshScope
@SuppressWarnings("deprecation")
public class OrderManagementServiceImpl implements OrderManagementService {

	@Value("${REQUESTED.ORDER.NOT.FOUND}")
	private String requestedOrderNotFound;

	@Value("${NO.RECORDS.FOUND}")
	private String noRecordsFound;

	@Value("${DUPLICATE.ITEM.EXCEPTION}")
	private String duplicateItemException;

	@Value("${TOO.MANY.ITEMS}")
	private String tooManyItems;

	@Value("${NO.ORDERIDS.FOUND}")
	private String noOrderIDsFound;

	@Value("${REQUESTED.ORDER.NOT.FOUND.IN.REQUIRED.RESTAURANT}")
	private String reqOrderNotFoundInReqRest;

	@Value("${CANNOT.UPDATE.CANCELLED.ORDER}")
	private String cannotUpdateCancelledOrder;

	@Value("${COULDNOT.UPDATE.ORDER}")
	private String couldnotUpdateOrder;

	@Value("${ORDER.ALREADY.CANCELLED}")
	private String orderAlreadyCancelled;

	@Value("${COULDNOT.CANCEL.ORDER}")
	private String couldnotCancelOrder;

	@Value("${ORDER.CANCELLED.SUCCESSFULLY}")
	private String orderCancelledSuccessfully;

	@Value("${TOTAL.ORDER.QUANTITY.LIMIT}")
	private String totalOrderQuantityLimit;

	@Value("${REQUESTED.RESTAURANT.NOT.FOUND}")
	private String requestedRestuarantNotFound;

	@Value("${HAZELCAST.CACHE.SWITCH}")
	private String hazelcastCacheSwitch;

	public String getHazelcastCacheSwitch() {
		return hazelcastCacheSwitch;
	}

	public void setHazelcastCacheSwitch(final String hazelcastCacheSwitchParam) {
		this.hazelcastCacheSwitch = hazelcastCacheSwitchParam;
	}

	@Autowired
	private OrderManagementRepository orderManagementRepository;

	@Autowired
	public HazelcastInstance hazelcastInstance;

	@Autowired
	private CustomerManagementProxy customerManagementProxy;

	@Autowired
	private HttpSession httpSession;

	private static final String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";

	public Long getCustomerIdFromCustomerService() {
		ResponseEntity<CustomerIdResponse> response = null;
		response = customerManagementProxy.getCustomerWithId((String) httpSession.getAttribute(X_ACCESS_TOKEN));
		if (response == null || response.getBody() == null) {
			throw new OrderNotFoundException(
					"Couldnt find the customer! This customer has not registered or is an inactive customer");
		}
		String id = response.getBody().getCustomerId();
		Long customerId = new Long(id);
		if (customerId == -1) {
			throw new OrderNotFoundException("Couldnot fetch customerId as Customer service took too long to respond");
		}
		return customerId;
	}

	public IMap<String, Order> orderMap() {
		return hazelcastInstance.getMap("order");
	}

	public IMap<String, List<Order>> orderCustMap() {
		return hazelcastInstance.getMap("customer");
	}

	public IMap<String, List<Order>> orderCustRestMap() {
		return hazelcastInstance.getMap("custRest");
	}

	public IMap<String, List<Order>> orderRestMap() {
		return hazelcastInstance.getMap("restaurant");
	}

	@Override
	public Order fetchOrder(long id) {
		if ("TRUE".equalsIgnoreCase(hazelcastCacheSwitch) && orderMap().containsKey(String.valueOf(id))) {
			return orderMap().get(String.valueOf(id));
		} else {
			Order order = orderManagementRepository.findByOrderId(id);
			if (order == null) {
				throw new OrderNotFoundException(requestedOrderNotFound);
			}
			return order;
		}
	}

	@Override
	public List<Order> fetchAllOrders(Integer pageNumber, Integer count) {
		Long customerId = getCustomerIdFromCustomerService();
		if (pageNumber == null && count == null && "TRUE".equalsIgnoreCase(hazelcastCacheSwitch)
				&& orderCustMap().containsKey(String.valueOf(customerId))) {
			List<Order> list = orderCustMap().get(String.valueOf(customerId));
			return list;
		} else {
			return returnAllOrders(customerId, pageNumber, count);
		}
	}

	private List<Order> returnAllOrders(Long customerId, Integer pageNumber, Integer count) {
		List<Order> orders = null;
		if (pageNumber == null && count == null) {
			orders = orderManagementRepository.findByCustomerId(customerId, null);
		} else {
			orders = orderManagementRepository.findByCustomerId(customerId, new PageRequest(pageNumber, count));
		}
		if (orders == null || orders.isEmpty()) {
			throw new OrderNotFoundException(noRecordsFound);
		}
		if (pageNumber == null && count == null) {
			orderCustMap().put(String.valueOf(customerId), orders);
		}
		return orders;
	}

	@Override
	public List<Order> fetchOrdersOfCustomerByRestaurantId(String id) {
		Long customerId = getCustomerIdFromCustomerService();
		StringBuilder builder = new StringBuilder().append(String.valueOf(id)).append("CR")
				.append(String.valueOf(customerId));
		if ("TRUE".equalsIgnoreCase(hazelcastCacheSwitch) && orderCustRestMap().containsKey(builder.toString())) {
			List<Order> list = orderCustRestMap().get(builder.toString());
			return list;
		} else {
			List<Order> orders = orderManagementRepository.findByRestaurantIdAndCustomerId(id, customerId);
			if (orders == null || orders.isEmpty()) {
				throw new OrderNotFoundException(requestedOrderNotFound);
			}
			return orders;
		}
	}

	@Override
	public List<Order> fetchOrdersByRestaurantId(String id, Integer pageNumber, Integer count) {
		if (pageNumber == null && count == null && "TRUE".equalsIgnoreCase(hazelcastCacheSwitch)
				&& orderRestMap().containsKey(String.valueOf(id))) {
			return orderRestMap().get(String.valueOf(id));
		} else {
			List<Order> orders = null;
			if (pageNumber == null && count == null) {
				orders = orderManagementRepository.findByRestaurantId(id, null);
			} else {
				orders = orderManagementRepository.findByRestaurantId(id, new PageRequest(pageNumber, count));
			}
			if (orders == null || orders.isEmpty()) {
				throw new OrderNotFoundException(requestedRestuarantNotFound);
			}
			if (pageNumber == null && count == null) {
				orderRestMap().put(id, orders);
			}
			return orders;
		}
	}

	@Override
	@Transactional
	public Order placeOrder(Order order) {
		order.setOrderId(getRandomValueForOrderId());
		Order orderResponse = addOrderToDb(order, OrderStatusType.PLACED);
		//populateCacheMap(orderResponse);
		return orderResponse;
	}

	private Order addOrderToDb(Order order, OrderStatusType status) {
		Long customerId = getCustomerIdFromCustomerService();
		validateQuantityForTotalItems(order);
		setPaymentInfo(order);
		checkForDuplicateValues(order);
		order.setOrderStatus(status);
		order.setCustomerId(customerId);
		Order orderResponse = orderManagementRepository.save(order);
		return orderResponse;
	}

	private void populateCacheMap(Order orderResponse) {
		orderMap().put(String.valueOf(orderResponse.getOrderId()), orderResponse);
		if (!orderCustMap().containsKey(String.valueOf(orderResponse.getCustomerId()))) {
			List<Order> orders = new ArrayList<>();
			orders.add(orderResponse);
			orderCustMap().put(String.valueOf(orderResponse.getCustomerId()), orders);
		} else {
			List<Order> list = orderCustMap().get(String.valueOf(orderResponse.getCustomerId()));
			list.add(orderResponse);
			orderCustMap().put(String.valueOf(orderResponse.getCustomerId()), list);
		}
		if (!orderRestMap().containsKey(String.valueOf(orderResponse.getRestaurantId()))) {
			List<Order> orders = new ArrayList<>();
			orders.add(orderResponse);
			orderRestMap().put(String.valueOf(orderResponse.getRestaurantId()), orders);
		} else {
			List<Order> list = orderRestMap().get(String.valueOf(orderResponse.getRestaurantId()));
			list.add(orderResponse);
			orderRestMap().put(String.valueOf(orderResponse.getRestaurantId()), list);
		}
		StringBuilder custRestId = new StringBuilder().append(orderResponse.getRestaurantId()).append("CR")
				.append(orderResponse.getCustomerId());
		if (!orderCustRestMap().containsKey(custRestId.toString())) {
			List<Order> orders = new ArrayList<>();
			orders.add(orderResponse);
			orderCustRestMap().put(custRestId.toString(), orders);
		} else {
			List<Order> list = orderCustRestMap().get(custRestId.toString());
			list.add(orderResponse);
			orderCustRestMap().put(custRestId.toString(), list);
		}
	}

	private void validateQuantityForTotalItems(Order order) {
		int totalQuantity = 0;
		for (Item item : order.getItemList()) {
			totalQuantity = totalQuantity + item.getQuantity();
		}
		if (totalQuantity > 1000) {
			throw new ItemsException(totalOrderQuantityLimit);
		}
	}

	private void checkForDuplicateValues(Order order) {
		Set<String> itemNames = new HashSet<>();
		for (Item item : order.getItemList()) {
			itemNames.add(item.getItemName().toUpperCase().trim());
		}
		if (itemNames.size() != order.getItemList().size()) {
			throw new ItemsException(duplicateItemException);
		}
		if (itemNames.size() > 50) {
			throw new ItemsException(tooManyItems);
		}
	}

	private void setPaymentInfo(Order order) {
		Transaction transaction = new Transaction();
		transaction.setTransactionId(Long.toHexString(Double.doubleToLongBits(Math.random())).toUpperCase());
		transaction.setPaymentMode(PaymentMode.CARD);
		transaction.setPaymentStatus(PaymentStatusType.SUCCESS);
		order.setTransaction(transaction);
	}

	@Override
	@Transactional
	public Order updateOrder(Order order) {
		if (order.getOrderId() == null) {
			throw new OrderNotFoundException(noOrderIDsFound);
		}
		List<Order> orders = orderManagementRepository.findByOrderIdAndRestaurantId(order.getOrderId(),
				order.getRestaurantId());
		if (orders == null || orders.isEmpty()) {
			throw new OrderNotFoundException(reqOrderNotFoundInReqRest);
		}
		if (orders.get(0).getOrderStatus().equals(OrderStatusType.CANCELLED)) {
			throw new OrderNotFoundException(cannotUpdateCancelledOrder);
		}
		int rowsAffected = orderManagementRepository.deleteByOrderId(order.getOrderId());
		if (rowsAffected != 1) {
			throw new OrderNotFoundException(couldnotUpdateOrder);
		}
		Order orderResponse = addOrderToDb(order, OrderStatusType.UPDATED);
		//populateCacheAfterUpdateCall(orderResponse);
		return orderResponse;
	}

	private void populateCacheAfterUpdateCall(Order orderResponse) {
		orderMap().replace(String.valueOf(orderResponse.getOrderId()), orderResponse);
		List<Order> listForCustomer = orderCustMap().get(String.valueOf(orderResponse.getCustomerId())) != null
				? orderRestMap().get(String.valueOf(orderResponse.getCustomerId()))
				: new ArrayList<>();
		for (Order orderFromCache : listForCustomer) {
			if (orderFromCache.getOrderId().equals(orderResponse.getOrderId())) {
				listForCustomer.remove(orderFromCache);
				listForCustomer.add(orderResponse);
				orderCustMap().put(String.valueOf(orderResponse.getCustomerId()), listForCustomer);
				break;
			}
		}

		List<Order> listForRest = orderRestMap().get(String.valueOf(orderResponse.getRestaurantId())) != null
				? orderRestMap().get(String.valueOf(orderResponse.getRestaurantId()))
				: new ArrayList<>();
		for (Order orderFromCache : listForRest) {
			if (orderFromCache.getOrderId().equals(orderResponse.getOrderId())) {
				listForRest.remove(orderFromCache);
				listForRest.add(orderResponse);
				orderRestMap().put(String.valueOf(orderResponse.getRestaurantId()), listForRest);
				break;
			}
		}
		StringBuilder custRestId = new StringBuilder().append(orderResponse.getRestaurantId()).append("CR")
				.append(orderResponse.getCustomerId());
		List<Order> listCustRest = orderCustRestMap().get(custRestId.toString());
		for (Order orderFromCache : listCustRest) {
			if (orderFromCache.getOrderId().equals(orderResponse.getOrderId())) {
				listCustRest.remove(orderFromCache);
				listCustRest.add(orderResponse);
				orderCustRestMap().put(custRestId.toString(), listCustRest);
				break;
			}
		}
	}

	@Override
	public String cancelOrder(long id) {
		Order order = fetchOrder(id);
		if (order.getOrderStatus().equals(OrderStatusType.CANCELLED)) {
			return orderAlreadyCancelled;
		}
		int rowsAffected = orderManagementRepository.cancelOrderById(id, OrderStatusType.CANCELLED);
		if (rowsAffected != 1) {
			throw new OrderNotFoundException(couldnotCancelOrder);
		} else {
			order.setOrderStatus(OrderStatusType.CANCELLED);
			//populateCacheAfterUpdateCall(order);
		}
		return orderCancelledSuccessfully;
	}

	public long getRandomValueForOrderId() {
		Random rand = new Random();
		long num = rand.nextInt(9000000) + 1000000;
		return num;
	}
}
