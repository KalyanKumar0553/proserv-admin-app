package com.src.proserv.main.exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AbstractRuntimeException.class)
    public ResponseEntity<?> handleResourceNotFoundException(AbstractRuntimeException ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),true,ex.getErrorCode());
//    	ex.printStackTrace();
        return ResponseEntity.status(ex.getErrorCode()).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
    	ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),true,HttpStatus.INTERNAL_SERVER_ERROR.value());
//    	ex.printStackTrace();
    	return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
    	Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        ErrorDetails errorDetails = new ErrorDetails(new Date(), errors.toString() ,true,HttpStatus.BAD_REQUEST.value());
//    	ex.printStackTrace();
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
