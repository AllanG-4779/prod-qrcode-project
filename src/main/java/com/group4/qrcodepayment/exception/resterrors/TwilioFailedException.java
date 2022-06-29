package com.group4.qrcodepayment.exception.resterrors;

public class TwilioFailedException  extends RuntimeException{


       public TwilioFailedException(String message){
           super(message);
       }
}
