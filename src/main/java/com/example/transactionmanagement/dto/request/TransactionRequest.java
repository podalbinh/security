package com.example.transactionmanagement.dto.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.example.transactionmanagement.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Represents a request object for creating or updating a transaction.
 */
@Data
public class TransactionRequest {

    /**
     * The ID of the transaction.
     */
    @NotNull(message = Constants.NOT_NULL)
    private Long id;

    /**
     * The transaction ID associated with the transaction.
     */
    @NotNull(message = Constants.NOT_NULL)
    private String transactionId;

    /**
     * The account number associated with the transaction.
     */
    @NotNull(message = Constants.NOT_NULL)
    private String account;

    /**
     * The amount of debt in the transaction.
     */
    @NotNull(message = Constants.NOT_NULL)
    private Double inDebt;

    /**
     * The amount of credit in the transaction.
     */
    @NotNull(message = Constants.NOT_NULL)
    private Double have;

    /**
     * The timestamp when the transaction occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Date time;
}
