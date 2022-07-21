package com.group4.qrcodepayment.exception.resterrors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankLinkedException extends  Exception{
    private final String debugMessage;
    public BankLinkedException(String message, String debugMessage){
        super(message);
        this.debugMessage = debugMessage;

    }
}
