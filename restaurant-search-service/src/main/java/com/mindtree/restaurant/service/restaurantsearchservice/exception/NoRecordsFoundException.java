package com.mindtree.restaurant.service.restaurantsearchservice.exception;

public class NoRecordsFoundException extends RuntimeException {

	public NoRecordsFoundException(String statusCode, String status, String message) {
		super(message);
	}

}
