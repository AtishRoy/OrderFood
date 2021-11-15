package com.mindtree.review.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidRestaurantIdFormatException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 282532761940537108L;

	public InvalidRestaurantIdFormatException(String message) {
        super(message);
    }
}
