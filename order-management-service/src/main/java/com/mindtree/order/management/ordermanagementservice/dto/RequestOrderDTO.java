package com.mindtree.order.management.ordermanagementservice.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Request Object", description = "Request object to create and update order")
@Data
public class RequestOrderDTO {
	
	@ApiModelProperty(notes = "Order ID")
	private String orderId;
	
	@ApiModelProperty(required = true, notes = "Restaurant ID")
	@NotNull(message = "Restuarant ID cannot be null")
	@NotBlank(message = "Restuarant ID cannot be blank")
	@NotEmpty(message = "Restuarant ID cannot be empty")
	private String restaurantId;

	@ApiModelProperty(notes = "Items ordered")
	@NotNull(message = "There should be items in the order")
	@NotEmpty(message = "Items list cannot be empty")
	@Valid
	private List<ItemDTO> itemList;

	@ApiModelProperty(notes = "Total Price")
	@NotNull(message = "Price cannot be null")
	@NotBlank(message = "Price cannot be blank")
	@NotEmpty(message = "Price cannot be empty")
	@Range(min=1, max=10000, message="Price should be between 1 to 10000")
	private String totalPrice;

	@ApiModelProperty(notes = "Delivery address")
	@NotNull(message = "Delivery address cannot be null")
	@NotBlank(message = "Delivery address cannot be blank")
	@NotEmpty(message = "Delivery address cannot be empty")
	@Size(min=5, max=75, message="Address size should be of length 5 to 75")
	private String deliveryAddress;
	
	@ApiModelProperty(notes = "Contact number")
	@NotNull(message = "Contact number cannot be null")
	@NotBlank(message = "Contact number cannot be null")
	@Size(min=6, max=10, message="Contact number seems to be invalid")
	@Pattern(regexp = "^[7-9][0-9]{9}$")
	private String contactNumber;
}
