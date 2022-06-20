package com.group4.qrcodepayment.exception.resterrors;

public class PhoneOrEmailExistsException extends RuntimeException {

       public PhoneOrEmailExistsException(String message){
           super(message);


       }
}
