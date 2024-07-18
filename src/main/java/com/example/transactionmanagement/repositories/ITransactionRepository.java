package com.example.transactionmanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transactionmanagement.entities.Transaction;


/**
 * Repository interface for managing Transaction entities.
 */
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

}
