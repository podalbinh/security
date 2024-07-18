package com.example.transactionmanagement.controllers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.transactionmanagement.dto.request.TransactionRequest;
import com.example.transactionmanagement.dto.response.TransactionResponse;
import com.example.transactionmanagement.services.impl.TransactionServiceImpl;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
      private final TransactionServiceImpl transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(Pageable pageable) {
        Page<TransactionResponse> transactions = transactionService.findAll(pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        TransactionResponse transaction = transactionService.get(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<Long> createTransaction(@RequestBody TransactionRequest transactionRequest) throws Exception {
        Long id = transactionService.create(transactionService.encryptTransactionRequest(transactionRequest));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) throws Exception {
        return ResponseEntity.ok( transactionService.update(id,transactionService.encryptTransactionRequest(transactionRequest)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
