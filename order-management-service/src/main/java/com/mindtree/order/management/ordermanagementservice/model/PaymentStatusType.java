package com.mindtree.order.management.ordermanagementservice.model;

public enum PaymentStatusType {

	SUCCESS("Payment Success"), FAILURE("Payment Failure");

	private String value;

	public String getValue() {
		return value;
	}

	PaymentStatusType(String value) {
		this.value = value;
	}
}
