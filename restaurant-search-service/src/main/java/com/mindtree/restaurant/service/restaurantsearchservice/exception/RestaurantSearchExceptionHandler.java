package com.mindtree.restaurant.service.restaurantsearchservice.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestaurantSearchExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NoRecordsFoundException.class)
	public ResponseEntity<ExceptionResponse> resourceNotFound(java.lang.Exception ex, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity<ExceptionResponse> invalidRequest(java.lang.Exception exception, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}

}
