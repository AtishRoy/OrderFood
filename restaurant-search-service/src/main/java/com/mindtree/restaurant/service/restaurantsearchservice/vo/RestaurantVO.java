package com.mindtree.restaurant.service.restaurantsearchservice.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Restaurant Details", description = "Item Details")
public class RestaurantVO {
	private String restaurantId;
	private String restaurantName;
	private String restaurantAddress;
	private String averageCostForTwo;
	private String restaurantCategory;
	private List<String> cuisinesAvailble;
	private List<ItemVO> itemsAvailableList;
	private String restaurantOpeningTime;
	private String restaurantClosingTime;
	private String restaurantRating;
	private String websiteLink;
	private String phoneNumber;

	public RestaurantVO() {
	}

}