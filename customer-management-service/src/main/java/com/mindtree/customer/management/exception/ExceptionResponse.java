/**
 *
 */
package com.mindtree.customer.management.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * <pre>
 * <b>Description : </b>
 * CustomizedResponseEntityExceptionHandler.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@Data
@AllArgsConstructor
public class ExceptionResponse {

	private Date timestamp;
	private int status;
	private String error;
	private String message;
	private String details;
}
