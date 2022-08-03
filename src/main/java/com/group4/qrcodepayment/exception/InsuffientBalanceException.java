package com.group4.qrcodepayment.exception;

public class InsuffientBalanceException extends Exception{
    public InsuffientBalanceException(String message){
        super(message);
    }
}
