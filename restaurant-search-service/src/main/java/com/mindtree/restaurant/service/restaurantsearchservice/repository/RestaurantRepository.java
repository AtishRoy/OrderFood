package com.mindtree.restaurant.service.restaurantsearchservice.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.mindtree.restaurant.service.restaurantsearchservice.model.Restaurant;

@Repository
public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {

	List<Restaurant> findAll();

}
