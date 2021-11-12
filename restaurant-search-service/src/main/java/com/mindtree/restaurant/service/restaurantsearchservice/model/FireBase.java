package com.mindtree.restaurant.service.restaurantsearchservice.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class FireBase {

	private Identities identities;
	private String sign_in_provider;
}
