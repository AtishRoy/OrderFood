package com.mindtree.order.management.ordermanagementservice.mapper;

import java.util.ArrayList;
import java.util.List;

import com.mindtree.order.management.ordermanagementservice.dto.ConfirmOrderDTO;
import com.mindtree.order.management.ordermanagementservice.dto.ItemDTO;
import com.mindtree.order.management.ordermanagementservice.dto.OrderDTO;
import com.mindtree.order.management.ordermanagementservice.dto.RequestOrderDTO;
import com.mindtree.order.management.ordermanagementservice.model.Item;
import com.mindtree.order.management.ordermanagementservice.model.Order;

public class EntityDTOMapper {

	public static Order mapDTOToEntity(RequestOrderDTO orderDTO) {
		Order order = new Order();
		if (orderDTO.getOrderId() != null) {
			order.setOrderId(Long.parseLong(orderDTO.getOrderId()));
		}
		order.setRestaurantId(orderDTO.getRestaurantId());
		order.setTotalPrice(Float.parseFloat(orderDTO.getTotalPrice()));
		List<Item> itemList = new ArrayList<>();
		for (ItemDTO itemDTO : orderDTO.getItemList()) {
			itemList.add(mapItemDTOToEntity(itemDTO));
		}
		order.setItemList(itemList);
		order.setContactNumber(orderDTO.getContactNumber());
		order.setDeliveryAddress(orderDTO.getDeliveryAddress());
		return order;
	}

	private static Item mapItemDTOToEntity(ItemDTO itemDTO) {
		Item item = new Item();
		item.setItemName(itemDTO.getItemName());
		item.setQuantity(Integer.parseInt(itemDTO.getQuantity()));
		return item;
	}

	public static ConfirmOrderDTO mapEntityToDTO(Order order) {
		ConfirmOrderDTO confirmOrderDTO = new ConfirmOrderDTO();
		confirmOrderDTO.setOrderId(order.getOrderId());
		confirmOrderDTO.setOrderStatus(order.getOrderStatus().name());
		if (order.getTransaction() != null) {
			confirmOrderDTO.setPaymentMode(order.getTransaction().getPaymentMode().name());
			confirmOrderDTO.setPaymentStatus(order.getTransaction().getPaymentStatus().name());
			confirmOrderDTO.setTransactionId(order.getTransaction().getTransactionId());
		}
		return confirmOrderDTO;
	}

	public static OrderDTO mapEntityToOrderDTO(Order order) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(order.getOrderId());
		orderDTO.setRestaurantId(order.getRestaurantId().toString());
		orderDTO.setCustomerId(order.getCustomerId().toString());
		orderDTO.setTotalPrice(order.getTotalPrice().toString());
		orderDTO.setOrderStatus(order.getOrderStatus().name());
		orderDTO.setContactNumber(order.getContactNumber());
		orderDTO.setDeliveryAddress(order.getDeliveryAddress());
		if (order.getTransaction() != null) {
			orderDTO.setPaymentMode(order.getTransaction().getPaymentMode().name());
			orderDTO.setPaymentStatus(order.getTransaction().getPaymentStatus().name());
			orderDTO.setTransactionId(order.getTransaction().getTransactionId());
		}
		List<ItemDTO> itemList = new ArrayList<>();
		for (Item item : order.getItemList()) {
			ItemDTO itemDTO = new ItemDTO();
			itemDTO.setItemName(item.getItemName());
			itemDTO.setQuantity(item.getQuantity().toString());
			itemList.add(itemDTO);
		}
		orderDTO.setItemList(itemList);
		return orderDTO;
	}

}
