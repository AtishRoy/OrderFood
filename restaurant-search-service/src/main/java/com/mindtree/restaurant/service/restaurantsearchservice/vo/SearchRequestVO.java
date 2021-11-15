package com.mindtree.restaurant.service.restaurantsearchservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchRequestVO {

	private String restaurantName;
	private String overallRating;
	private String budget;
	private String cuisine;
	private String city;
	private String dishName;

}
