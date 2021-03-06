/**
 *
 */
package com.mindtree.order.management.ordermanagementservice.model;

/**
 * @author Atish Roy
 *
 */
public enum PaymentMode {

	CARD("Payment Card"),

	UPI("Unified Payments Interface"),

	COD("Cash On Delivery");

	private String value;

	public String getValue() {
		return value;
	}

	PaymentMode(String valueParam) {
		this.value = valueParam;
	}
}
