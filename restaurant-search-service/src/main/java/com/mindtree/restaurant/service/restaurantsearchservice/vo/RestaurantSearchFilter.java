package com.mindtree.restaurant.service.restaurantsearchservice.vo;

import lombok.Data;

@Data
public class RestaurantSearchFilter {

	private String name;

	private String overallRating;
	private String budget;
	private String cuisine;
	private String city;
	private String dishName;
	/*
	 * private String distance; private String latitude; private String longitutde;
	 */

}
