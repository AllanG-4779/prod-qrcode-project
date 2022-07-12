package com.group4.qrcodepayment.exception;

import io.jsonwebtoken.JwtException;

public class InvalidJWTException extends JwtException {


    public InvalidJWTException(String message) {
        super(message);
    }
}
