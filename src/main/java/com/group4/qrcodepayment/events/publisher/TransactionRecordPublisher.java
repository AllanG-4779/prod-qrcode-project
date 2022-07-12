package com.group4.qrcodepayment.events.publisher;

import com.group4.qrcodepayment.events.event.TransactionRecordedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionRecordPublisher  {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishTransactionRecordedEvent(String accountNumber,
                                                String transactionId,
                                                String bankCode){

        TransactionRecordedEvent transactionRecorded = new TransactionRecordedEvent(this);
        transactionRecorded.setTransactionRef(transactionId);
        transactionRecorded.setAccountNumber(accountNumber);
        transactionRecorded.setBankCode(bankCode);
        applicationEventPublisher.publishEvent(transactionRecorded);

    }
}
