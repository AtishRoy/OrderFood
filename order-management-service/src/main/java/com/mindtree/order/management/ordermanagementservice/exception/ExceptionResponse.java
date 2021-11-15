package com.mindtree.order.management.ordermanagementservice.exception;

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
