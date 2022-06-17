package com.group4.qrcodepayment.exception.resterrors;

import org.springframework.security.authentication.BadCredentialsException;

public class InvalidUsernameOrPasswordException extends BadCredentialsException {

    public InvalidUsernameOrPasswordException(String msg) {
        super(msg);
    }
}
