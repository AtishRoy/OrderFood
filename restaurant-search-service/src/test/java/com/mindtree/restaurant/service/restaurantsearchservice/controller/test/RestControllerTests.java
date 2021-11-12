package com.mindtree.restaurant.service.restaurantsearchservice.controller.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import com.mindtree.restaurant.service.restaurantsearchservice.RestaurantSearchServiceApplication;
import com.mindtree.restaurant.service.restaurantsearchservice.builder.SearchResponseBuilder;
import com.mindtree.restaurant.service.restaurantsearchservice.exception.NoRecordsFoundException;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Address;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Category;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Item;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.model.SubCategory;
import com.mindtree.restaurant.service.restaurantsearchservice.service.impl.RestaurantServiceImpl;
import com.mindtree.restaurant.service.restaurantsearchservice.validator.Validator;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ReviewVO;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = RestaurantSearchServiceApplication.class)
public class RestControllerTests {

	@Autowired
	private WebApplicationContext context;

	protected MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
        validator = Mockito.mock(Validator.class);
	}

	@Autowired
	MappingJackson2HttpMessageConverter jacksonConverter;
	@MockBean
	public RestaurantServiceImpl service;
	@MockBean
	public SearchResponseBuilder builder;
	public Validator validator;

	@TestConfiguration
	public class ModifyModifyManagementServiceTestContextConfiguration {
		@Bean
		public RestaurantServiceImpl globalSummitService() {
			return service;
		}
	}
	

	@Test
	public void testSearch() {
		List<Restaurant> restaurants = buildRestaurant();
		try {
			Mockito.when(service.getRestaurantsBySearchParam(Mockito.anyString())).thenReturn(restaurants);
			MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
			request.when().get("/restaurant/search?query=vinod").then().statusCode(200);
			Mockito.verify(builder, Mockito.times(1)).buildResponse(Mockito.anyList(), Mockito.any(HttpStatus.class),
					Mockito.anyString(), Mockito.anyString());
		} catch (NoRecordsFoundException e) {
			Assert.fail();
		}
	}

	@Test
	public void testSearchWhenNoRecordsFound() {
		Mockito.when(service.getRestaurantsBySearchParam(Mockito.anyString())).thenReturn(new ArrayList<Restaurant>());
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		request.when().get("/restaurant/search?query=vinod").then().statusCode(404);
		Mockito.verify(builder, Mockito.times(0)).buildResponse(Mockito.anyList(), Mockito.any(HttpStatus.class),
				Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testRstaurantMenu() {
		List<Restaurant> list = buildRestaurant();
		Optional<Restaurant> value = Optional.of(list.get(0));
		try {
			Mockito.when(service.getRestaurantMenu(Mockito.anyString())).thenReturn(value);
			MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
			request.when().get("/restaurant/getrestaurantmenu?restaurantId=VK11").then().statusCode(200);
			Mockito.verify(builder, Mockito.times(1)).buildItems(Mockito.anyList());
		} catch (NoRecordsFoundException e) {
			Assert.fail();
		}
	}

	@Test
	public void testRstaurantMenuWhenNoRecordsFound() throws NoRecordsFoundException {
		Optional<Restaurant> value = Optional.of(new Restaurant());
		Mockito.when(service.getRestaurantMenu(Mockito.anyString())).thenReturn(value);
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		request.when().get("/restaurant/getrestaurantmenu?restaurantId=A2B\r\n" + "").then().statusCode(404);
		Mockito.verify(builder, Mockito.times(0)).buildItems(Mockito.anyList());
	}

	@Test
	public void testSearchByFilter() throws ApplicationException, NoRecordsFoundException {
		List<Restaurant> restaurants = buildRestaurant();
		Mockito.when(service.getRestaurantsBySearchCriteria(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(restaurants);
		Mockito.when(validator.isValidRequest("320","4")).thenReturn(true);
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		request.when().get("/restaurant/searchbyfilter?name=BBQNation").then().statusCode(404);
	}

	@Test
	public void testSearchByFilterIfNOresordsFound() throws ApplicationException, NoRecordsFoundException {
		Mockito.when(service.getRestaurantsBySearchCriteria(Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(new ArrayList<Restaurant>());
		Mockito.when(validator.isValidRequest("320","4")).thenReturn(true);
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		request.when().get("/restaurant/searchbyfilter?name=vinod").then().statusCode(404);
	}

	@Test
	public void testupdateReviews() throws ApplicationException, NoRecordsFoundException {
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		List<ReviewVO> review = new ArrayList<>();
		review.add(buildNewReview("11112", "4.5"));
		request.body(convertToJson(review));
		request.header("Content-Type", "application/json");
		request.when().put("/restaurant").then().statusCode(200);
	}

	@Test
	public void testupdateReviewsWhenException() throws ApplicationException, NoRecordsFoundException {
		Mockito.doThrow(new NoRecordsFoundException("200", "Fail", "No records Found")).when(service)
				.updateAverageRating(Mockito.anyList());
		MockMvcRequestSpecification request = RestAssuredMockMvc.given().mockMvc(mvc);
		List<ReviewVO> review = new ArrayList<>();
		review.add(buildNewReview("11112", "4.5"));
		request.body(convertToJson(review));
		request.header("Content-Type", "application/json");
		request.when().put("/restaurant").then().statusCode(404);
	}

	private ReviewVO buildNewReview(String restId, String avgRating) {
		ReviewVO reviewVO = new ReviewVO();
		reviewVO.setRestaurantId(restId);
		reviewVO.setAvgRating(avgRating);
		return reviewVO;
	}

	private String convertToJson(List<ReviewVO> request) {

		MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
		try {
			jacksonConverter.write(request, MediaType.APPLICATION_JSON, outputMessage);
			System.out.println(outputMessage.getBody().toString());
			return outputMessage.getBody().toString();
		} catch (HttpMessageNotWritableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	private List<Restaurant> buildRestaurant() {
		List<Restaurant> list = new ArrayList<Restaurant>();
		Collection<Category> categories = new ArrayList<Category>();
		List<SubCategory> categories2 = new ArrayList<SubCategory>();
		List<Item> iteList = new ArrayList<>();
		Item item = Item.builder().description("Delciaoius pizza").itemName("VEG surepeme").price(200f).build();
		iteList.add(item);
		SubCategory subCateg = SubCategory.builder().subCategoryName("PIZZA").itemList(iteList).build();
		categories2.add(subCateg);
		Category categ = Category.builder().categoryName("Veg").subCategoryList(categories2).build();
		categories.add(categ);
		Restaurant restaurant = Restaurant.builder()
				.address(Address.builder().addressLine("#58,1st main road").area("parvathipuram").city("Bangalore")
						.landmark("Sajjan Rao").pinCode("560004").state("Karanataka").build())
				.budget(350).name("BBQNation").overallRating(4.5f).phoneNumber("9988776655").restaurantId("VK11")
				.categoryList(categories).build();
		list.add(restaurant);
		return list;
	}

}
