package com.example.transactionmanagement.dto.response;

import java.util.Date;

import com.example.transactionmanagement.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private String account;
    private Double inDebt;
    private Double have;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Date time;
}
