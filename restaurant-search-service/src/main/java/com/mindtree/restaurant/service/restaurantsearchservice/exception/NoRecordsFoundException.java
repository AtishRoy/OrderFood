package com.mindtree.restaurant.service.restaurantsearchservice.exception;

public class NoRecordsFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9149921728751266815L;

	public NoRecordsFoundException(String statusCode, String status, String message) {
		super(message);
	}

}
