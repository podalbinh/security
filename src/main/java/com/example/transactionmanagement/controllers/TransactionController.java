package com.example.transactionmanagement.controllers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transactionmanagement.dto.request.TransactionRequest;
import com.example.transactionmanagement.dto.response.TransactionResponse;
import com.example.transactionmanagement.services.impl.TransactionServiceImpl;
import com.example.transactionmanagement.utils.RSAUtil;

/**
 * REST Controller for managing transactions.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    /**
     * Constructor for TransactionController.
     * 
     * @param transactionService The transaction service to handle business logic.
     */
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * GET /transactions : Get all transactions.
     * 
     * @param pageable Pagination information.
     * @return ResponseEntity with a page of TransactionResponse.
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponse> transactions = transactionService.findAll(pageable);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /transactions/{id} : Get a transaction by its ID.
     * 
     * @param id The ID of the transaction.
     * @return ResponseEntity with the TransactionResponse.
     * @throws InvalidKeyException    If an invalid key is encountered during encryption.
     * @throws NoSuchAlgorithmException If no such algorithm exists.
     * @throws NoSuchProviderException If no such provider exists.
     * @throws NoSuchPaddingException If no such padding exists.
     * @throws IllegalBlockSizeException If an illegal block size is encountered during encryption.
     * @throws BadPaddingException    If bad padding is encountered during encryption.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        TransactionResponse transaction = transactionService.get(id);
        return ResponseEntity.ok(transaction);
    }

    /**
     * POST /transactions : Create a new transaction.
     * 
     * @param transactionRequest The transaction request body.
     * @return ResponseEntity with the ID of the created transaction.
     * @throws Exception If an error occurs during transaction creation or encryption.
     */
    @PostMapping
    public ResponseEntity<Long> createTransaction(@RequestBody TransactionRequest transactionRequest) throws Exception {
        Long id = transactionService.create(transactionService.encryptTransactionRequest(transactionRequest));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * PUT /transactions/{id} : Update an existing transaction.
     * 
     * @param id The ID of the transaction to update.
     * @param transactionRequest The updated transaction request body.
     * @return ResponseEntity with the updated TransactionResponse.
     * @throws Exception If an error occurs during transaction update or encryption.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) throws Exception {
        return ResponseEntity.ok(transactionService.update(id, transactionService.encryptTransactionRequest(transactionRequest)));
    }

    /**
     * DELETE /transactions/{id} : Delete a transaction by its ID.
     * 
     * @param id The ID of the transaction to delete.
     * @return ResponseEntity indicating successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }


     /**
     * Processes a transfer request by creating two transaction records:
     * one for the debit transaction on the source account and one for the credit transaction on the destination account.
     * Ensures that none of the parameters are null before proceeding with the transaction.
     *
     * @param transactionId the transaction ID associated with the transfer
     * @param sourceAccount the source account number from which the amount is debited
     * @param destinationAccount the destination account number to which the amount is credited
     * @param amount the amount to be transferred
     * @return a response indicating the result of the transfer operation
     * @throws InvalidKeySpecException 
     * @throws UnsupportedEncodingException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     */
    @PostMapping("/process")
    public ResponseEntity<String> processTransfer(
            @RequestParam String transactionId,
            @RequestParam String sourceAccount,
            @RequestParam String destinationAccount,
            @RequestParam Double amount) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
            transactionService.processTransfer(RSAUtil.encrypt(transactionId), RSAUtil.encrypt(sourceAccount), RSAUtil.encrypt(destinationAccount), String.valueOf(amount));
            return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
