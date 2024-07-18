package com.example.transactionmanagement.entities;

import javax.persistence.*;

import com.example.transactionmanagement.security.converts.AESConverter;
import com.example.transactionmanagement.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Convert(converter = AESConverter.class)
    @Column(name = "account", nullable = false,length = 1000000000)
    private String account;

    @Column(name = "in_debt")
    private Double inDebt;

    @Column(name = "have")
    private Double have;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Date time;

    public Transaction() {
    }

    public Transaction(String transactionId, String account, Double inDebt, Double have, Date time) {
        this.transactionId = transactionId;
        this.account = account;
        this.inDebt = inDebt;
        this.have = have;
        this.time = time;
    }

    public Long getId() {
        return id;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getInDebt() {
        return inDebt;
    }

    public void setInDebt(Double inDebt) {
        this.inDebt = inDebt;
    }

    public Double getHave() {
        return have;
    }

    public void setHave(Double have) {
        this.have = have;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
