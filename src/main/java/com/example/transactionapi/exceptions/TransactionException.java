package com.example.transactionapi.exceptions;

public class TransactionException extends RuntimeException{

    public  TransactionException(String message){
        super(message);
    }
}
