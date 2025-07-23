package com.learning.tracker.exception;

public class TransactionException extends BusinessException{

    public TransactionException(String message){
        super(message, "TRANSACTION_ERROR");
    }

    public TransactionException(String message, Throwable cause) {
        super(message, "TRANSACTION_ERROR", cause);
    }
}
