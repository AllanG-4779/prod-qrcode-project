package com.group4.qrcodepayment.exception.resterrors;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ReactiveHttpInputMessage;

@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFailedException extends Exception{

    public RegistrationFailedException(String message){
        super(message);
    }

}
