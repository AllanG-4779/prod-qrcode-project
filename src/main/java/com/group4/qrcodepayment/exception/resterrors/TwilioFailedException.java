package com.group4.qrcodepayment.exception.resterrors;

import lombok.Getter;
import lombok.Setter;
@Getter@Setter

public class TwilioFailedException  extends RuntimeException{
        private String userMessage;

       public TwilioFailedException(String message, String userMessage){
           super(message);
           this.userMessage  = userMessage;
       }
}
