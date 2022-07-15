package com.group4.qrcodepayment.exception.resterrors;

public class TransactionNotFoundException extends Exception{

    public TransactionNotFoundException(String mess)    {
        super(mess);
    }
}
