package com.mindtree.restaurant.service.restaurantsearchservice.model;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Identities {

	private List<String> name;
	private List<String> email;
}
