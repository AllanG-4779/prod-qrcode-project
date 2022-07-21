package com.group4.qrcodepayment.events.event;

import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter

public class TransactionRecordedEvent extends ApplicationEvent {
    private TransferResponse transferResponse;
    public TransactionRecordedEvent(Object source) {
        super(source);
    }
}
