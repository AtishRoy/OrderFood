package com.mindtree.restaurant.service.restaurantsearchservice.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "SearchRestaurantResponse", description = "Item Details") 
public class SearchRestaurantResponseVO {
	
	private String numberOfRecords;
	private String statusCode;
	private String status;
	private String message;
	private List<RestaurantVO> restaurantDetails;

}
