package com.mindtree.restaurant.service.restaurantsearchservice.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Item Details", description = "Item Details")
public class ItemVO {

	private String itemName;

	private String itemDesc;

	private String itemPrice;

}
