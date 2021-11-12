/**
 * 
 */
package com.mindtree.zuulapigatewayserver.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Ranjith Ranganathan
 *
 */
@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<java.lang.Object> globalExceptionHandeling(java.lang.Exception ex, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}	
	
}
