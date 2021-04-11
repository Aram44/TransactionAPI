package com.example.transactionapi.repository;


import com.example.transactionapi.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

}