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
 * @version $Revision: 1 $ $Date: 2018-09-25 02:33:55 AM $
 * @author $Author: nithya.pranesh $ 
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
