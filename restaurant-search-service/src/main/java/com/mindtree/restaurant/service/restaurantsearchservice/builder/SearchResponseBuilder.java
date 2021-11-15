package com.mindtree.restaurant.service.restaurantsearchservice.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mindtree.restaurant.service.restaurantsearchservice.model.Address;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Category;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Cuisine;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Item;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.model.SubCategory;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ItemVO;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.RestaurantVO;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.SearchRestaurantResponseVO;

@Component
public class SearchResponseBuilder {

	public SearchRestaurantResponseVO buildResponse(List<Restaurant> restaurantList, HttpStatus noContent, String status, String message) {
		SearchRestaurantResponseVO responseVO = new SearchRestaurantResponseVO();
		responseVO.setNumberOfRecords(String.valueOf(restaurantList.size()));
		responseVO.setStatusCode(noContent.toString());
		responseVO.setStatus(status);
		responseVO.setMessage(message);
		List<RestaurantVO> restaurantVOList = new ArrayList<>();
		for (Restaurant restaurantReq : restaurantList) {
			RestaurantVO rest = new RestaurantVO();
			rest.setRestaurantId(restaurantReq.getRestaurantId());
			rest.setRestaurantName(restaurantReq.getName());
			rest.setRestaurantCategory(buildCategory(restaurantReq.getCategoryList()));
			rest.setRestaurantOpeningTime(restaurantReq.getOpeningTime());
			rest.setRestaurantClosingTime(restaurantReq.getClosingTime());
			rest.setRestaurantRating(String.valueOf(restaurantReq.getOverallRating()));
			rest.setWebsiteLink(restaurantReq.getWebsiteLink());
			rest.setPhoneNumber(restaurantReq.getPhoneNumber());
			rest.setAverageCostForTwo(String.valueOf(restaurantReq.getBudget()));
			rest.setCuisinesAvailble(buildCuisines(restaurantReq.getCuisineList()));
			rest.setRestaurantAddress(buildRestaurantAddress(restaurantReq.getAddress()));
			rest.setItemsAvailableList(buildItems(restaurantReq.getCategoryList()));
			restaurantVOList.add(rest);
		}
		responseVO.setRestaurantDetails(restaurantVOList);
		return responseVO;
	}

	private String buildCategory(Collection<Category> categoryList) {
		StringBuilder builder = new StringBuilder();
		for (Category category : categoryList) {
			builder.append(category.getCategoryName());
			builder.append(" ");
		}
		return builder.toString();
	}

	private String buildRestaurantAddress(Address address) {
		StringBuilder builder = null;
		if (address != null) {
			builder = new StringBuilder();
			if (address.getAddressLine() != null) {
				builder.append(address.getAddressLine());
				builder.append(",");
			}
			if (address.getArea() != null) {
				builder.append(address.getArea());
				builder.append(",");
			}
			if (address.getCity() != null) {
				builder.append(address.getCity());
				builder.append(",");
			}
			if (address.getPinCode() != null) {
				builder.append(address.getPinCode());
				builder.append(",");
			}
			if (address.getLandmark() != null) {
				builder.append(address.getLandmark());
				builder.append(",");
			}
			if (address.getState() != null) {
				builder.append(address.getState());
			}
			return builder.toString();
		}
		return null;
	}

	public List<ItemVO> buildItems(Collection<Category> categoryList) {
		List<ItemVO> itemVOs = new ArrayList<>();
		for (Category category : categoryList) {
			for (SubCategory subCategory : category.getSubCategoryList()) {
				for (Item item : subCategory.getItemList()) {
					ItemVO itemVO = new ItemVO();
					itemVO.setItemDesc(item.getDescription());
					itemVO.setItemName(item.getItemName());
					itemVO.setItemPrice(String.valueOf(item.getPrice()));
					itemVOs.add(itemVO);
				}
			}

		}
		return itemVOs;
	}

	private List<String> buildCuisines(Collection<Cuisine> cuisineList) {
		List<String> cuisine = null;
		if (cuisineList != null) {
			cuisine = new ArrayList<>();
			for (Cuisine cuisine2 : cuisineList) {
				cuisine.add(cuisine2.getCuisineName());
			}

		}
		return cuisine;
	}

}
