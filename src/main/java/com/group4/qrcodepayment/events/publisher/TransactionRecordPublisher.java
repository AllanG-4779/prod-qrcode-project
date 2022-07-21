package com.group4.qrcodepayment.events.publisher;

import com.group4.qrcodepayment.events.event.TransactionRecordedEvent;
import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionRecordPublisher  {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishTransactionRecordedEvent(TransferResponse transferResponse){

        TransactionRecordedEvent transactionRecorded = new TransactionRecordedEvent(this);
        transactionRecorded.setTransferResponse(transferResponse);

        applicationEventPublisher.publishEvent(transactionRecorded);

    }
}
