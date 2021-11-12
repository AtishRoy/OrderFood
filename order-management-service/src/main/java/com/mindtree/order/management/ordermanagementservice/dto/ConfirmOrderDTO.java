package com.mindtree.order.management.ordermanagementservice.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Response Object", description = "Response object for create and update order")
public class ConfirmOrderDTO {

	@ApiModelProperty(notes = "Order Id")
	private Long orderId;
	
	@ApiModelProperty(notes = "Order Id")
	private String orderStatus;
	
	@ApiModelProperty(notes = "Transaction Id")
	private String transactionId;
	
	@ApiModelProperty(notes = "Payment mode")
	private String paymentMode;
	
	@ApiModelProperty(notes = "Payment status")
	private String paymentStatus;

}
