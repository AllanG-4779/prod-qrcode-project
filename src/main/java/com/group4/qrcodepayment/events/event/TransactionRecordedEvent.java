package com.group4.qrcodepayment.events.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter

public class TransactionRecordedEvent extends ApplicationEvent {
    private String accountNumber;
    private String transactionRef;
    private String bankCode;
    public TransactionRecordedEvent(Object source) {
        super(source);
    }
}
