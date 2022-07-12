package com.group4.qrcodepayment.events.publisher;

import com.group4.qrcodepayment.dto.RegistrationDto;
import com.group4.qrcodepayment.events.event.LoginRegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;

@Component
public class LoginRegistrationEventPublisher {
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void authPublisher(String phoneNumber){
        LoginRegisterEvent loginRegisterEvent = new LoginRegisterEvent(this);
        loginRegisterEvent.setPhoneNumber(phoneNumber);
        eventPublisher.publishEvent(loginRegisterEvent);

    }
}
