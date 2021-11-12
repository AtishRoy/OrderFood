package com.mindtree.restaurant.service.restaurantsearchservice.service.test;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mindtree.restaurant.service.restaurantsearchservice.exception.NoRecordsFoundException;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Address;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Category;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Cuisine;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Item;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.model.SubCategory;
import com.mindtree.restaurant.service.restaurantsearchservice.repository.RestaurantRepository;
import com.mindtree.restaurant.service.restaurantsearchservice.service.impl.RestaurantServiceImpl;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ReviewVO;

import groovy.util.NodeBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestaurantServiceTest {
	@MockBean
	private ElasticsearchTemplate elasticsearchTemplate;

	private RestaurantServiceImpl service;
	@MockBean
	private RestaurantRepository restaurantRepository;
	
	
	@Configuration 
    @EnableElasticsearchRepositories(basePackages = "org.springframework.data.elasticsearch.repositories") 
    static class Config { 
 
        @Bean 
        public ElasticsearchOperations elasticsearchTemplate() { 
            Client client=Mockito.mock(TransportClient.class);
			return new ElasticsearchTemplate(client);
        } 
    } 

	@Before
	public void before() {
		service = new RestaurantServiceImpl();
		Mockito.mock(Client.class);
		elasticsearchTemplate = Mockito.mock(ElasticsearchTemplate.class);
		service.setElasticsearchTemplate(elasticsearchTemplate);
		service.setRestaurantRepository(restaurantRepository);

	}

	@Test
	public void testSearchByCriteriaWithName() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("name", "BBQNation"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithRating() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("overAllRating", "4"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithBudget() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("budget", "12"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithCuisine() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("cuisine", "Chinese"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithlat() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("lat", "12"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithLon() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("lon", "-98.0"));

		SearchQuery restList = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(restList, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSearchByCriteriaWithArea() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("area", "RR Nagar"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testGetItemsOfRestaurant() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("name", "BBQNation"));

		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchCriteria("BBQNation", "4", "200", "Chinese", "Bnagalore", "Pulav");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	
	@Test
	public void testgetRestaurantsBySearchParam() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("name", "BBQNation"));
		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantsBySearchParam("BBQNation");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testsearchFoodItemsInRestaurant() throws NoRecordsFoundException {
		NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
		nativeSearch.withQuery(matchQuery("name", "BBQNation"));
		SearchQuery searchQuery = nativeSearch.build();
		Mockito.when(elasticsearchTemplate.queryForList(searchQuery, Restaurant.class)).thenReturn(buildMockResponse());
		service.getRestaurantMenu("BBQNation");
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());

	}

	@Test
	public void testSaveRestauarants() throws NoRecordsFoundException {
		Mockito.when(restaurantRepository.save(buildMockResponse().get(0))).thenReturn(buildMockResponse().get(0));
		service.save(buildMockResponse());
		assertResponse("BBQNation", "4.5", "350", "Chinese", "Bangalore", "VEG surepeme", buildMockResponse());
	}
	
	
	@Test
	public void testupdateReviews() throws NoRecordsFoundException {
		Optional<Restaurant> value = Optional.of(buildMockResponse().get(0));
		Mockito.when(restaurantRepository.findById(Mockito.anyString())).thenReturn(value);
		List<ReviewVO> review = new ArrayList<>();
		review.add(buildNewReview("11112", "4.5"));
		service.updateAverageRating(review);
	}
	private ReviewVO buildNewReview(String restId, String avgRating) {
		ReviewVO reviewVO = new ReviewVO();
		reviewVO.setRestaurantId(restId);
		reviewVO.setAvgRating(avgRating);
		return reviewVO;
	}


	private void assertResponse(String name, String rating, String budget, String cuisine, String city, String dishName,
			List<Restaurant> restList) {
		Restaurant rest = restList.get(0);
		ArrayList<Cuisine> cuisines = (ArrayList<Cuisine>) rest.getCuisineList();
		ArrayList<Category> categories = (ArrayList<Category>) rest.getCategoryList();
		Assert.assertEquals(name, rest.getName());
		Assert.assertEquals(cuisine, cuisines.get(0).getCuisineName());
		Assert.assertEquals(city, rest.getAddress().getCity());
		Assert.assertEquals(dishName, categories.get(0).getSubCategoryList().get(0).getItemList().get(0).getItemName());

	}

	private List<Restaurant> buildMockResponse() {
		return buildRestaurant();
		/*
		 * List<Restaurant> list = new ArrayList<Restaurant>();
		 * list.add(buildRetaurant()); return list;
		 */
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
		Restaurant restaurant = Restaurant.builder().cuisineList(cuisineList).closingTime("11:00 PM")
				.openingTime("12:30 AM").websiteLink("www.example.com")
				.address(Address.builder().addressLine("#58,1st main road").area("parvathipuram").city("Bangalore")
						.landmark("Sajjan Rao").pinCode("560004").state("Karanataka").build())
				.budget(350).name("BBQNation").overallRating(4.5f).phoneNumber("9988776655").restaurantId("VK11")
				.categoryList(categories).build();
		list.add(restaurant);
		return list;
	}

	/*
	 * private Restaurant buildRetaurant() { Restaurant restaurant = new
	 * Restaurant(); restaurant.setName("BBQNation");
	 * restaurant.setOverallRating(4F); restaurant.setBudget(200);
	 * Collection<Category> categoryList = new ArrayList<Category>(); Category e =
	 * new Category(); List<SubCategory> subCategoryList = new ArrayList<>();
	 * SubCategory sub = new SubCategory(); List<Item> itemList = new ArrayList<>();
	 * Item item = new Item(); item.setItemName("Pizza"); itemList.add(item);
	 * sub.setItemList(itemList); subCategoryList.add(sub);
	 * e.setSubCategoryList(subCategoryList); categoryList.add(e);
	 * restaurant.setCategoryList(categoryList); Collection<Cuisine> cuisineList =
	 * new ArrayList<>(); Cuisine cuisine = new Cuisine();
	 * cuisine.setCuisineName("Chinese"); cuisineList.add(cuisine);
	 * restaurant.setCuisineList(cuisineList); Address address = new Address();
	 * address.setArea("RR Nagar"); address.setCity("Bangalore");
	 * restaurant.setAddress(address); return restaurant; }
	 */

}
