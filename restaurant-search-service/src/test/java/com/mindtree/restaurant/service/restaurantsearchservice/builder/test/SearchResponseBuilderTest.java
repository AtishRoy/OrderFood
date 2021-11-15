package com.mindtree.restaurant.service.restaurantsearchservice.builder.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.mindtree.restaurant.service.restaurantsearchservice.builder.SearchResponseBuilder;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Address;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Category;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Cuisine;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Item;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.model.SubCategory;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.SearchRestaurantResponseVO;

public class SearchResponseBuilderTest {
	SearchResponseBuilder builder;

	@Test
	public void testBuilder() {
		builder = new SearchResponseBuilder();
		List<Restaurant> list = buildRestaurant();
		SearchRestaurantResponseVO response = builder.buildResponse(list, HttpStatus.OK, "", "");
		assertresponseDetails(response, true);
	}

	@Test
	public void testBuilderWhenAddressIsNull() {
		builder = new SearchResponseBuilder();
		List<Restaurant> list = buildRestaurant();
		list.get(0).setAddress(null);
		SearchRestaurantResponseVO response = builder.buildResponse(list, HttpStatus.OK, "", "");
		response.getRestaurantDetails().get(0).setRestaurantAddress(null);
		assertresponseDetails(response, false);
	}

	private void assertresponseDetails(SearchRestaurantResponseVO response, boolean isAdressAdded) {
		Assert.assertEquals("1", response.getNumberOfRecords());
		Assert.assertEquals("350.0", response.getRestaurantDetails().get(0).getAverageCostForTwo());
		Assert.assertEquals("Chinese", response.getRestaurantDetails().get(0).getCuisinesAvailble().get(0));
		Assert.assertEquals("Delicious pizza", response.getRestaurantDetails().get(0).getItemsAvailableList().get(0).getItemDesc());
		Assert.assertEquals("VEG surepeme", response.getRestaurantDetails().get(0).getItemsAvailableList().get(0).getItemName());
		Assert.assertEquals("200.0", response.getRestaurantDetails().get(0).getItemsAvailableList().get(0).getItemPrice());
		Assert.assertEquals("9988776655", response.getRestaurantDetails().get(0).getPhoneNumber());
		Assert.assertEquals("Veg", response.getRestaurantDetails().get(0).getRestaurantCategory().trim());
		Assert.assertEquals("11:00 PM", response.getRestaurantDetails().get(0).getRestaurantClosingTime());
		Assert.assertEquals("VK11", response.getRestaurantDetails().get(0).getRestaurantId());
		Assert.assertEquals("BBQNation", response.getRestaurantDetails().get(0).getRestaurantName());

		Assert.assertEquals("12:30 AM", response.getRestaurantDetails().get(0).getRestaurantOpeningTime());
		Assert.assertEquals("4.5", response.getRestaurantDetails().get(0).getRestaurantRating());
		Assert.assertEquals("www.example.com", response.getRestaurantDetails().get(0).getWebsiteLink());
		if (isAdressAdded) {
			Assert.assertEquals("#58,1st main road,parvathipuram,Bangalore,560004,Sajjan Rao,Karanataka", response.getRestaurantDetails().get(0).getRestaurantAddress());
		}
	}

	private List<Restaurant> buildRestaurant() {
		List<Restaurant> list = new ArrayList<Restaurant>();
		Collection<Category> categories = new ArrayList<Category>();
		List<SubCategory> categories2 = new ArrayList<SubCategory>();
		List<Item> iteList = new ArrayList<>();
		List<Cuisine> cuisineList = new ArrayList<Cuisine>();
		Cuisine cuisine = Cuisine.builder().cuisineName("Chinese").build();
		cuisineList.add(cuisine);
		Item item = Item.builder().description("Delicious pizza").itemName("VEG surepeme").price(200f).build();
		iteList.add(item);
		SubCategory subCateg = SubCategory.builder().subCategoryName("PIZZA").itemList(iteList).build();
		categories2.add(subCateg);
		Category categ = Category.builder().categoryName("Veg").subCategoryList(categories2).build();
		categories.add(categ);
		Restaurant restaurant = Restaurant.builder().cuisineList(cuisineList).closingTime("11:00 PM").openingTime("12:30 AM").websiteLink("www.example.com")
				.address(Address.builder().addressLine("#58,1st main road").area("parvathipuram").city("Bangalore").landmark("Sajjan Rao").pinCode("560004").state("Karanataka").build()).budget(350)
				.name("BBQNation").overallRating(4.5f).phoneNumber("9988776655").restaurantId("VK11").categoryList(categories).build();
		list.add(restaurant);
		return list;
	}

}
