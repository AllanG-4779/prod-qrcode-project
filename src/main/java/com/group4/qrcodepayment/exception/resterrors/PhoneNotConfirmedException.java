package com.group4.qrcodepayment.exception.resterrors;

import java.nio.file.AccessDeniedException;

public class PhoneNotConfirmedException extends Exception {
    public PhoneNotConfirmedException(String message){
        super(message);
    }
}
