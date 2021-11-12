package com.mindtree.customer.management.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public final ResponseEntity<java.lang.Object> handleCustomerNotFoundException(java.lang.Exception ex,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
                httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
    
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public final ResponseEntity<java.lang.Object> handleCustomerAlreadyFoundException(java.lang.Exception ex,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
                httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
    
    @ExceptionHandler(InvalidDateFormatException.class)
    public final ResponseEntity<java.lang.Object> handleInvalidDateFormatException(java.lang.Exception ex,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
                httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, httpStatus);
    }
    
    @ExceptionHandler(InvalidDataFormatException.class)
    public final ResponseEntity<java.lang.Object> handleInvalidDataFormatException(java.lang.Exception ex,
            WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), httpStatus.value(),
                httpStatus.getReasonPhrase(), ex.getMessage(), request.getDescription(false));

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
