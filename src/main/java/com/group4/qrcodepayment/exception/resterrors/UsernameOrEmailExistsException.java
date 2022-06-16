package com.group4.qrcodepayment.exception.resterrors;

public class UsernameOrEmailExistsException extends RuntimeException {

       public UsernameOrEmailExistsException(String message){
           super(message);


       }
}
