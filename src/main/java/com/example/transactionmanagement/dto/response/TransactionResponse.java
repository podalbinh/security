package com.example.transactionmanagement.dto.response;

import java.util.Date;

import com.example.transactionmanagement.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Represents a response object for a transaction.
 */
@Data
public class TransactionResponse {

    /**
     * The ID of the transaction.
     */
    private Long id;

    /**
     * The transaction ID associated with the transaction.
     */
    private String transactionId;

    /**
     * The account number associated with the transaction.
     */
    private String account;

    /**
     * The amount of debt in the transaction.
     */
    private Double inDebt;

    /**
     * The amount of credit in the transaction.
     */
    private Double have;

    /**
     * The timestamp when the transaction occurred.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Date time;
}
