package com.example.transactionmanagement.exception;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.transactionmanagement.configs.Translator;
import com.example.transactionmanagement.utils.Constants;
import com.example.transactionmanagement.utils.MessagesConstants;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Global exception handler for handling various exceptions across the application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Exception handler for DataAccessException.
     *
     * @param exception the exception that occurred
     * @param webRequest the current web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorDetails> handleDataAccessException(DataAccessException exception,
                                                                  WebRequest webRequest) {
        logErrorWithMasking(Constants.LOG_DATA_ACCESS_EXCEPTION,exception, webRequest); 
        ErrorDetails errorDetails = new ErrorDetails(new Date(),  exception.getMessage(),
        webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    
    /**
     * Exception handler for MethodArgumentNotValidException (Validation errors).
     *
     * @param ex the MethodArgumentNotValidException
     * @param request the current web request
     * @return ResponseEntity containing ErrorDetails
     */
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                     WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream()
        .map(error -> Translator.toLocale(error.getDefaultMessage()))
        .collect(Collectors.toList());                                                            
        ErrorDetails errorDetails = new ErrorDetails(new Date(),Translator.toLocale(MessagesConstants.VALIDATION_FAILED_MESSAGE), errorMessages.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

        /**
     * Exception handler to handle ResourceNotFoundException.
     *
     * @param exception   The ResourceNotFoundException to handle.
     * @param webRequest  The current web request.
     * @return ResponseEntity containing an ErrorDetails object and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest) {
        logErrorWithMasking(Constants.LOG_RESOURCE_NOT_FOUND_EXCEPTION,exception, webRequest); 
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(IllegalArgumentException exception,
                                                                        WebRequest webRequest) {
        logErrorWithMasking(Constants.LOG_RESOURCE_NOT_FOUND_EXCEPTION,exception, webRequest); 
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for general Exception.
     *
     * @param exception the exception that occurred
     * @param webRequest the current web request
     * @return ResponseEntity containing ErrorDetails
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest) {
        logErrorWithMasking(Constants.LOG_EXCEPTION, exception, webRequest);                                       
        ErrorDetails errorDetails = new ErrorDetails(new Date(),  exception.getMessage(),
               webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void logErrorWithMasking(String errorMessage, Exception exception, WebRequest webRequest) {
        log.error( errorMessage + maskSensitiveInfo(exception.getMessage()) + " - " + maskSensitiveInfo(webRequest.getDescription(false)));
    }

    private String maskSensitiveInfo(String originalMessage) {
        return originalMessage.replaceAll(Constants.NUMERIC_REGEX, Constants.MASKED)
                .replaceAll(Constants.DECIMAL_REGEX,Constants.MASKED)
                .replaceAll(Constants.DATE_TIME_REGEX, Constants.MASKED);
    }
}
