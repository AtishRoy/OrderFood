package com.mindtree.order.management.ordermanagementservice.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;

@RestControllerAdvice
public class OrderManagementExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<java.lang.Object> handleAllException(java.lang.Exception ex, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), "Exception occured in the database layer", request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public final ResponseEntity<java.lang.Object> handleNotFoundException(OrderNotFoundException ex,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
	@ExceptionHandler(FeignException.class)
	public final ResponseEntity<java.lang.Object> handleFeignException(FeignException ex,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
	@ExceptionHandler(ItemsException.class)
	public final ResponseEntity<java.lang.Object> handleItemException(ItemsException ex,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
	@ExceptionHandler(NumberFormatException.class)
	public final ResponseEntity<java.lang.Object> handleNumberFormatException(NumberFormatException ex,
			WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), "Input data mismatch", request.getDescription(false));

		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		Map<String, String> errorMap = new HashMap<>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
				httpStatus.getReasonPhrase(), "Input data mismatch", errorMap.toString());
		return new ResponseEntity<>(exceptionResponse, httpStatus);
	}
	
}
