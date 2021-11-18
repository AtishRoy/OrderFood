package com.mindtree.order.management.ordermanagementservice.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Order", description = "Order model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {

	@ApiModelProperty(notes = "Order ID")
	private long orderId;

	@ApiModelProperty(notes = "Restuarant ID")
	private String restaurantId;

	@ApiModelProperty(notes = "Customer ID")
	private String customerId;

	@ApiModelProperty(notes = "Items")
	private List<ItemDTO> itemList;

	@ApiModelProperty(notes = "Total Price")
	private String totalPrice;

	@ApiModelProperty(notes = "Delivery address")
	private String deliveryAddress;

	@ApiModelProperty(notes = "Contact number")
	private String contactNumber;

	@ApiModelProperty(notes = "Order status")
	private String orderStatus;

	@ApiModelProperty(notes = "Transaction Id")
	private String transactionId;

	@ApiModelProperty(notes = "Payment mode")
	private String paymentMode;

	@ApiModelProperty(notes = "Payment status")
	private String paymentStatus;
}
