package com.mindtree.customer.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * <pre>
 * <b>Description : </b>
 * CustomerNotFoundException.
 *
 * &#64;author $Author: Atish Roy $
 * </pre>
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5259597367529269214L;

	public CustomerNotFoundException(String arg0) {
		super(arg0);
	}
}
