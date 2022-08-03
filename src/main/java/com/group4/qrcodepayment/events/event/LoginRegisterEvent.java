package com.group4.qrcodepayment.events.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter

public class LoginRegisterEvent extends ApplicationEvent {
    private String phoneNumber;

    public LoginRegisterEvent(Object source) {
        super(source);
    }
}
