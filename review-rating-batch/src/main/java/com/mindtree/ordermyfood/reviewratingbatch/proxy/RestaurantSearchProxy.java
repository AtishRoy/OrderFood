package com.mindtree.ordermyfood.reviewratingbatch.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mindtree.ordermyfood.reviewratingbatch.model.ReviewandRating;

@FeignClient(name = "RESTAURANT-SEARCH-SERVICE")
public interface RestaurantSearchProxy {

	@PutMapping("/restaurant")
	public void updateAverageRating(@RequestHeader("X-ACCESS-TOKEN") String token,
			List<? extends ReviewandRating> reviews);

}
