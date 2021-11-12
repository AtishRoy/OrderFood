package com.mindtree.restaurant.service.restaurantsearchservice.service.impl;

import java.util.List;
import java.util.Optional;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.mindtree.restaurant.service.restaurantsearchservice.exception.NoRecordsFoundException;
import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;
import com.mindtree.restaurant.service.restaurantsearchservice.pagination.SearchPagination;
import com.mindtree.restaurant.service.restaurantsearchservice.repository.RestaurantRepository;
import com.mindtree.restaurant.service.restaurantsearchservice.service.RestaurantService;
import com.mindtree.restaurant.service.restaurantsearchservice.vo.ReviewVO;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	ElasticsearchOperations elasticsearchTemplate;

	@Override
	public List<Restaurant> save(List<Restaurant> restaurantList) {
		for (Restaurant restaurant2 : restaurantList) {
			restaurantRepository.save(restaurant2);
		}
		elasticsearchTemplate.refresh("restaurant");
		return restaurantList;
	}

	/*
	 * public String getAllRestaurant() { List<Restaurant> list=
	 * restaurantRepository.findAll(); return "Total Restaurants  : "+list.size(); }
	 */
	@Override
	public List<Restaurant> getRestaurantsBySearchCriteria(String name, String overallRating, String budget,
			String cuisine, String city, String dishName) throws NoRecordsFoundException {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		if (name != null) {
			query.filter(QueryBuilders.queryStringQuery("*" + name + "*").lenient(true).field("name"));
		}
		if (city != null) {
			query.filter(QueryBuilders.queryStringQuery("*" + city + "*").lenient(true).field("address.city"));
		}
		if (overallRating != null) {
			query.filter(QueryBuilders.rangeQuery("overallRating").gte(Float.parseFloat(overallRating)).lte(5.0));
		}
		if (budget != null) {
			query.filter(QueryBuilders.rangeQuery("budget").lte(Float.parseFloat(budget)));
		}
		if (cuisine != null) {
			query.filter(
					QueryBuilders.queryStringQuery("*" + cuisine + "*").lenient(true).field("cuisineList.cuisineName"));
		}
		if (dishName != null) {
			query.filter(QueryBuilders.queryStringQuery("*" + dishName + "*").lenient(true)
					.field("categoryList.subCategoryList.itemList.itemName"));
		}
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();
		build.setPageable(new SearchPagination());
		return elasticsearchTemplate.queryForList(build, Restaurant.class);
	}

	@Override
	public List<Restaurant> getRestaurantsBySearchParam(String searchParam) throws NoRecordsFoundException {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query.should(QueryBuilders.queryStringQuery(searchParam).lenient(true))
				.should(QueryBuilders.queryStringQuery("*" + searchParam + "*").lenient(true));
		NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(query).build();
		build.setPageable(new SearchPagination());
		return elasticsearchTemplate.queryForList(build, Restaurant.class);
	}

	@Override
	public Optional<Restaurant> getRestaurantMenu(String restaurantId) throws NoRecordsFoundException {
		return restaurantRepository.findById(restaurantId);
	}

	public void setElasticsearchTemplate(ElasticsearchOperations elasticsearchTemplate) {
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	public void setRestaurantRepository(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Override
    public void updateAverageRating(List<ReviewVO> reviews) {
        for (ReviewVO review : reviews) {
            Optional<Restaurant> restaurantOpt = restaurantRepository.findById(review.getRestaurantId());
            if (restaurantOpt.isPresent()) {
                Restaurant restaurant = restaurantOpt.get();
                restaurant.setOverallRating(Float.valueOf(review.getAvgRating()));
                restaurantRepository.save(restaurant);
            }
            else {
                throw new NoRecordsFoundException("404", "Not Found", "Restaurant not found");
            }
        }
    }   

}
