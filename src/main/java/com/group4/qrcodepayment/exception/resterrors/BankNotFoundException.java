package com.group4.qrcodepayment.exception.resterrors;


public class BankNotFoundException extends RuntimeException{
      public BankNotFoundException(String message){
          super(message);
      }
}
