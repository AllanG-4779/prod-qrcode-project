package com.group4.qrcodepayment.exception.resterrors;

public class OtpNotGeneratedException extends RuntimeException{
     public OtpNotGeneratedException(String message){
         super(message);
     }
}
