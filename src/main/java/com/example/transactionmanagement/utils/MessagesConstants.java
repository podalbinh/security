package com.example.transactionmanagement.utils;

import org.springframework.stereotype.Component;

/**
 * Constants for message keys used in transaction management.
 */
public class MessagesConstants {

    /**
     * Error message key for transaction not found.
     */
    public static final String TRANSACTION_NOT_FOUND_ERROR = "transaction.not-found";

    /**
     * Message key for validation failed.
     */
    public static final String VALIDATION_FAILED_MESSAGE = "transaction.validate";

    /**
     * Error message key for ID must not be null.
     */
    public static final String ID_NOT_NULL = "id.not-null";

    /**
     * Error message key for data must not be null.
     */
    public static final String DATA_NOT_NULL = "data.not-null";

    /**
     * Error message key for encrypted data must not be null.
     */
    public static final String ENCRYPT_DATA_NOT_NULL = "e-data.not-null";
}
