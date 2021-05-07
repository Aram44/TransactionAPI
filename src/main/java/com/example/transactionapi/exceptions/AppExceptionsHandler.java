package com.example.transactionapi.exceptions;

import com.example.transactionapi.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public class AppExceptionsHandler {

    @ExceptionHandler(value = {TransactionException.class})
    public ResponseEntity<Object> handleNullTransactionException(NullPointerException ex, WebRequest request){
         String errorMessageDescription = ex.getLocalizedMessage();
         if (errorMessageDescription == null)
             errorMessageDescription = ex.toString();
         ErrorMessage errorMessage = new ErrorMessage(new Date(), errorMessageDescription);
         return new ResponseEntity<>(errorMessage,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
