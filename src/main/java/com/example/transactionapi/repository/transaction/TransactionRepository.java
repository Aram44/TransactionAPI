package com.example.transactionapi.repository.transaction;

import com.example.transactionapi.model.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}