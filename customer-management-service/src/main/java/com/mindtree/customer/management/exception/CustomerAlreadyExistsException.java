package com.mindtree.customer.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * <pre>
 * <b>Description : </b>
 * CustomerAlreadyExistsException.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class CustomerAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1965022343253219967L;

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}

}
