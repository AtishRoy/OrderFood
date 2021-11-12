package com.mindtree.order.management.ordermanagementservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "Item", description = "Item model")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {

	@ApiModelProperty(notes = "Item name")
	@NotNull(message = "Item cannot be null")
	@NotBlank(message = "Item cannot be blank")
	@Size(min=1, max=50, message="Item name length should be between 1 and 50")
	private String itemName;

	@ApiModelProperty(notes = "Item Quantity")
	@NotNull(message = "Quantity cannot be null")
	@NotBlank(message = "Quantity cannot be blank")
	@NotEmpty(message = "Quantity cannot be empty")
	@Pattern(regexp = "^[0-9]*$", message = "Invalid Quanity")
	@Range(min=1, max=500, message="Quantity cannot be 0 / Quantity cannot exceed 500")
	private String quantity;
	
}
