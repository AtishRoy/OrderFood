package com.mindtree.order.management.ordermanagementservice.model;

public enum OrderStatusType {
	
	PLACED("Order is placed"),
	UPDATED("Order is updated"),
	ACCEPTED("Order accepted by the restuarant"),
	PREPARED("Order is prepared and ready for the delivery"),
	DELIVERED("Order is delivered"),
	CANCELLED("Order is cancelled"),
	FAILURE("Failed to place the order");
	
	private String value;
	
	public String getValue() {
		return value;
	}
	
	OrderStatusType(String value) {
		this.value = value;
	}

}
