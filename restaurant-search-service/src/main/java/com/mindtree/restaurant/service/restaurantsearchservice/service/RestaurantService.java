package com.mindtree.restaurant.service.restaurantsearchservice.service;

import java.util.List;
import java.util.Optional;

import com.mindtree.restaurant.service.restaurantsearchservice.exception.NoRecordsFoundException;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ReviewVO;

public interface RestaurantService {

	List<Restaurant> save(List<Restaurant> restaurantList);

	List<Restaurant> getRestaurantsBySearchCriteria(String name, String overallRating, String budget, String cuisine,
			String city, String dishName) throws NoRecordsFoundException;

	List<Restaurant> getRestaurantsBySearchParam(String searchParam) throws NoRecordsFoundException;

	Optional<Restaurant> getRestaurantMenu(String restaurantName) throws NoRecordsFoundException;
	/*
	 * String getAllRestaurant();
	 */

	void updateAverageRating(List<ReviewVO> reviews);
}
