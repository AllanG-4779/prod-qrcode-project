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

    public void authPublisher(RegistrationDto message){
        LoginRegisterEvent loginRegisterEvent = new LoginRegisterEvent(message);
        loginRegisterEvent.setRegistrationDto(message);
        eventPublisher.publishEvent(loginRegisterEvent);

    }
}
