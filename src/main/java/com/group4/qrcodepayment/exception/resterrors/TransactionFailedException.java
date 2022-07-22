package com.group4.qrcodepayment.exception.resterrors;

public class TransactionFailedException extends RuntimeException{
    public TransactionFailedException(String message){
        super(message);
    }
}
