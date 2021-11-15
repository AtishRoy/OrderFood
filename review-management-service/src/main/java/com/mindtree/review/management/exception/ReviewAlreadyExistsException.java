/**
 * 
 */
package com.mindtree.review.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Atish Roy
 *
 */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class ReviewAlreadyExistsException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3576838481767388681L;

	/**
     * @param message
     */
    public ReviewAlreadyExistsException(String message) {
        super(message);
    }

}
