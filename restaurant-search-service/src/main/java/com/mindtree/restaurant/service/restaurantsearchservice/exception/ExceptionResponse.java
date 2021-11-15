package com.mindtree.restaurant.service.restaurantsearchservice.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {

	private Date timestamp;
	private int status;
	private String error;
	private String message;
	private String details;

}
