package com.mindtree.customer.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * <pre>
 * <b>Description : </b>
 * CustomerNotFoundException.
 * 
 * @version $Revision: 1 $ $Date: 2018-09-25 02:33:55 AM $
 * @author $Author: nithya.pranesh $ 
 * </pre>
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String arg0) {
        super(arg0);
    }
}
