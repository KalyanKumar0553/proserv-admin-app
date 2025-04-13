package com.src.proserv.main.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.src.proserv.main.exceptions.AbstractRuntimeException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AbstractRuntimeException.class)
    public ResponseEntity<?> handleResourceNotFoundException(AbstractRuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),true,ex.getErrorCode());
    	ex.printStackTrace();
        return ResponseEntity.status(ex.getErrorCode()).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), "Request Failed with Internal Server Error",true,HttpStatus.INTERNAL_SERVER_ERROR.value());
//    	ex.printStackTrace();
    	return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
