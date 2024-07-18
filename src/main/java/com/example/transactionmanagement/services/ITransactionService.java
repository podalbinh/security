package com.example.transactionmanagement.services;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.transactionmanagement.dto.request.TransactionRequest;
import com.example.transactionmanagement.dto.response.TransactionResponse;

public interface ITransactionService {

    /**
     * Retrieve all transactions with pagination.
     *
     * @param pageable pagination information
     * @return a page of transaction responses
     */
    public Page<TransactionResponse> findAll(Pageable pageable);

    /**
     * Retrieve an transaction by ID.
     *
     * @param id the ID of the transaction to retrieve
     * @return the transaction response
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchProviderException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    public TransactionResponse get(final Long id) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

    /**
     * Create a new transaction.
     *
     * @param transactionRequest the request object containing transaction details
     * @return the ID of the created transaction
     * @throws Exception 
     */
    public Long create(final TransactionRequest transactionRequest) throws Exception;

    /**
     * Update an existing transaction.
     *
     * @param id the ID of the transaction to update
     * @param transactionRequest the request object containing updated transaction details
     * @throws Exception 
     */
    public TransactionResponse update(final Long id, final TransactionRequest transactionRequest) throws Exception;

    /**
     * Delete an transaction by ID.
     *
     * @param id the ID of the transaction to delete
     */
    public void delete(final Long id);
}
