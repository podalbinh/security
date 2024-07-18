package com.example.transactionmanagement.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class representing details of an error response.
 */
@Getter
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;

    /**
     * Get the timestamp when the error occurred.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Get the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the details of the error.
     *
     * @return the details of the error
     */
    public String getDetails() {
        return details;
    }
}
