package com.example.transactionmanagement.entities;

import javax.persistence.*;
import com.example.transactionmanagement.security.converts.AESConverter;
import com.example.transactionmanagement.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @Column(name = "account", nullable = false, length = 1000000000)
    private String account;

    @Column(name = "in_debt")
    private Double inDebt;

    @Column(name = "have")
    private Double have;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_FORMAT)
    private Date time;

    /**
     * Default constructor for Transaction entity.
     */
    public Transaction() {
    }

    /**
     * Constructor to initialize Transaction entity with parameters.
     *
     * @param transactionId The transaction ID.
     * @param account       The encrypted account number.
     * @param inDebt        The debt amount in the transaction.
     * @param have          The credit amount in the transaction.
     * @param time          The timestamp of the transaction.
     */
    public Transaction(String transactionId, String account, Double inDebt, Double have, Date time) {
        this.transactionId = transactionId;
        this.account = account;
        this.inDebt = inDebt;
        this.have = have;
        this.time = time;
    }

    /**
     * Get the ID of the transaction.
     *
     * @return The transaction ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Get the transaction ID.
     *
     * @return The transaction ID.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Set the transaction ID.
     *
     * @param transactionId The transaction ID to set.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Get the encrypted account number.
     *
     * @return The encrypted account number.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Set the encrypted account number.
     *
     * @param account The encrypted account number to set.
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Get the debt amount in the transaction.
     *
     * @return The debt amount.
     */
    public Double getInDebt() {
        return inDebt;
    }

    /**
     * Set the debt amount in the transaction.
     *
     * @param inDebt The debt amount to set.
     */
    public void setInDebt(Double inDebt) {
        this.inDebt = inDebt;
    }

    /**
     * Get the credit amount in the transaction.
     *
     * @return The credit amount.
     */
    public Double getHave() {
        return have;
    }

    /**
     * Set the credit amount in the transaction.
     *
     * @param have The credit amount to set.
     */
    public void setHave(Double have) {
        this.have = have;
    }

    /**
     * Get the timestamp of the transaction.
     *
     * @return The timestamp of the transaction.
     */
    public Date getTime() {
        return time;
    }

    /**
     * Set the timestamp of the transaction.
     *
     * @param time The timestamp to set.
     */
    public void setTime(Date time) {
        this.time = time;
    }
}
