package com.example.transactionmanagement.services.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transactionmanagement.configs.Translator;
import com.example.transactionmanagement.dto.request.TransactionRequest;
import com.example.transactionmanagement.dto.response.TransactionResponse;
import com.example.transactionmanagement.entities.Transaction;
import com.example.transactionmanagement.exception.ResourceNotFoundException;
import com.example.transactionmanagement.repositories.ITransactionRepository;
import com.example.transactionmanagement.services.ITransactionService;
import com.example.transactionmanagement.utils.Constants;
import com.example.transactionmanagement.utils.MessagesConstants;
import com.example.transactionmanagement.utils.RSAUtil;

/**
 * Implementation of the Transaction Service interface.
 */
@Service
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    public TransactionServiceImpl(ITransactionRepository transactionRepository, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves all transactions from the database with pagination.
     *
     * @param pageable Pagination information.
     * @return Page of TransactionResponse objects.
     */
    @Override
    public Page<TransactionResponse> findAll(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(transaction -> modelMapper.map(transaction, TransactionResponse.class));
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction to retrieve.
     * @return TransactionResponse object if found.
     * @throws InvalidKeyException       If there is an issue with encryption keys.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is unavailable.
     * @throws NoSuchProviderException   If a specified security provider is unavailable.
     * @throws NoSuchPaddingException    If the padding scheme specified in a decryption operation is invalid.
     * @throws IllegalBlockSizeException If a block size error occurs in decryption.
     * @throws BadPaddingException       If the padding is invalid in a decryption operation.
     * @throws ResourceNotFoundException If the transaction with the given ID does not exist.
     */
    @Override
    public TransactionResponse get(Long id) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        if (id == null) {
            throw new IllegalArgumentException(MessagesConstants.ID_NOT_NULL);
        }
        Optional<Transaction> transaction = transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return modelMapper.map(transaction.get(), TransactionResponse.class);
        } else {
            throw new ResourceNotFoundException(
                    Translator.toLocale(MessagesConstants.TRANSACTION_NOT_FOUND_ERROR) + id);
        }
    }

    /**
     * Creates a new transaction.
     *
     * @param transactionRequest The transaction request to create.
     * @return The ID of the newly created transaction.
     * @throws InvalidKeyException       If there is an issue with encryption keys.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is unavailable.
     * @throws NoSuchProviderException   If a specified security provider is unavailable.
     * @throws NoSuchPaddingException    If the padding scheme specified in encryption is invalid.
     * @throws IllegalBlockSizeException If a block size error occurs in encryption.
     * @throws BadPaddingException       If the padding is invalid in encryption.
     * @throws UnsupportedEncodingException If the encoding is not supported during encryption.
     * @throws InvalidKeySpecException 
     */
    @Override
    @Transactional
    public Long create(TransactionRequest transactionRequest) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException, InvalidKeySpecException {
        TransactionRequest decryptedRequest = decryptTransactionRequest(transactionRequest);
        Transaction transaction = modelMapper.map(decryptedRequest, Transaction.class);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return savedTransaction.getId();
    }

    /**
     * Updates an existing transaction by ID.
     *
     * @param id                 The ID of the transaction to update.
     * @param transactionRequest The updated transaction request.
     * @return Updated TransactionResponse object.
     * @throws InvalidKeyException       If there is an issue with encryption keys.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is unavailable.
     * @throws NoSuchProviderException   If a specified security provider is unavailable.
     * @throws NoSuchPaddingException    If the padding scheme specified in encryption is invalid.
     * @throws IllegalBlockSizeException If a block size error occurs in encryption.
     * @throws BadPaddingException       If the padding is invalid in encryption.
     * @throws UnsupportedEncodingException If the encoding is not supported during encryption.
     * @throws InvalidKeySpecException 
     * @throws ResourceNotFoundException If the transaction with the given ID does not exist.
     */
    @Override
    @Transactional
    public TransactionResponse update(Long id, TransactionRequest transactionRequest) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        if (id == null) {
            throw new IllegalArgumentException(MessagesConstants.ID_NOT_NULL);
        }
        TransactionRequest decryptedRequest = decryptTransactionRequest(transactionRequest);

        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            modelMapper.map(decryptedRequest, transaction);
            transactionRepository.save(transaction);
            TransactionResponse response = modelMapper.map(transaction, TransactionResponse.class);
            response.setTransactionId(RSAUtil.decrypt(response.getTransactionId()));
            response.setAccount(RSAUtil.decrypt(response.getAccount()));
            return response;
        } else {
            throw new ResourceNotFoundException(
                    Translator.toLocale(MessagesConstants.TRANSACTION_NOT_FOUND_ERROR) + id);
        }
    }

    /**
     * Deletes a transaction by ID.
     *
     * @param id The ID of the transaction to delete.
     * @throws ResourceNotFoundException If the transaction with the given ID does not exist.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(MessagesConstants.ID_NOT_NULL);
        }
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            transactionRepository.delete(existingTransaction.get());
        } else {
            throw new ResourceNotFoundException(
                    Translator.toLocale(MessagesConstants.TRANSACTION_NOT_FOUND_ERROR) + id);
        }
    }

    /**
     * Encrypts sensitive data in the transaction request.
     *
     * @param request The transaction request to encrypt.
     * @return Encrypted transaction request.
     * @throws InvalidKeyException       If there is an issue with encryption keys.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is unavailable.
     * @throws NoSuchProviderException   If a specified security provider is unavailable.
     * @throws NoSuchPaddingException    If the padding scheme specified in encryption is invalid.
     * @throws IllegalBlockSizeException If a block size error occurs in encryption.
     * @throws BadPaddingException       If the padding is invalid in encryption.
     * @throws UnsupportedEncodingException If the encoding is not supported during encryption.
     * @throws InvalidKeySpecException 
     */
    public TransactionRequest encryptTransactionRequest(TransactionRequest request) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        TransactionRequest encryptedRequest = new TransactionRequest();
        modelMapper.map(request, encryptedRequest);
        encryptedRequest.setTransactionId(RSAUtil.decrypt(request.getTransactionId()));
        encryptedRequest.setAccount(RSAUtil.decrypt(request.getAccount()));
        return encryptedRequest;
    }

    /**
     * Decrypts sensitive data in the transaction request.
     *
     * @param encryptedRequest The encrypted transaction request to decrypt.
     * @return Decrypted transaction request.
     * @throws InvalidKeyException       If there is an issue with decryption keys.
     * @throws NoSuchAlgorithmException  If a required cryptographic algorithm is unavailable.
     * @throws NoSuchProviderException   If a specified security provider is unavailable.
     * @throws NoSuchPaddingException    If the padding scheme specified in decryption is invalid.
     * @throws IllegalBlockSizeException If a block size error occurs in decryption.
     * @throws BadPaddingException       If the padding is invalid in decryption.
     * @throws UnsupportedEncodingException If the encoding is not supported during decryption.
     * @throws InvalidKeySpecException 
     */
    public TransactionRequest decryptTransactionRequest(TransactionRequest encryptedRequest)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        encryptedRequest.setTransactionId(RSAUtil.decrypt(encryptedRequest.getTransactionId()));
        encryptedRequest.setAccount(RSAUtil.decrypt(encryptedRequest.getAccount()));
        return encryptedRequest;
    }
    
   /**
     * Processes a transfer transaction by creating two transaction records:
     * one for the debit transaction on the source account and one for the credit transaction on the destination account.
     * Ensures that none of the parameters are null before proceeding with the transaction.
     *
     * @param transactionId the transaction ID associated with the transfer
     * @param sourceAccount the source account number from which the amount is debited
     * @param destinationAccount the destination account number to which the amount is credited
     * @param amount the amount to be transferred
    * @throws InvalidKeySpecException 
    * @throws UnsupportedEncodingException 
    * @throws BadPaddingException 
    * @throws IllegalBlockSizeException 
    * @throws NoSuchPaddingException 
    * @throws NoSuchAlgorithmException 
    * @throws InvalidKeyException 
     * @throws IllegalArgumentException if any parameter is null
     */
    public void processTransfer(String transactionId, String sourceAccount, String destinationAccount, String amount) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeySpecException {
        if (transactionId == null) {
            throw new IllegalArgumentException(Constants.TRANSACTIONID_NOT_NULL);
        }
        if ( sourceAccount == null ) {
            throw new IllegalArgumentException(Constants.SOURCEACCOUNT_NOT_NULL);
        }
        if (destinationAccount == null ) {
            throw new IllegalArgumentException(Constants.DESTINATIONACCOUNT_NOT_NULL);
        }
        if ( amount == null) {
            throw new IllegalArgumentException(Constants.AMOUNT_NOT_NULL);
        }
        Date now = new Date();
        Transaction debitTransaction = new Transaction();
        debitTransaction.setTransactionId(RSAUtil.decrypt(transactionId));
        debitTransaction.setAccount(RSAUtil.decrypt(sourceAccount));
        debitTransaction.setInDebt(Double.parseDouble(RSAUtil.decrypt(amount)));
        debitTransaction.setHave(0.0);
        debitTransaction.setTime(now);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setTransactionId(RSAUtil.decrypt(transactionId));
        creditTransaction.setAccount(RSAUtil.decrypt(destinationAccount));
        creditTransaction.setInDebt(0.0);
        creditTransaction.setHave(Double.parseDouble(RSAUtil.decrypt(amount)));
        creditTransaction.setTime(now);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }
}
