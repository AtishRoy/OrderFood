/**
 * 
 */
package com.mindtree.review.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * $Author: Atish Roy
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 204347848750557529L;

	/**
     * @param arg0
     */
    public ReviewNotFoundException(String arg0) {
        super(arg0);
    }
}
