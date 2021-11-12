package com.mindtree.ordermyfood.reviewratingbatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewandRating {

	private String restaurantId;
	private float avgRating;

}
