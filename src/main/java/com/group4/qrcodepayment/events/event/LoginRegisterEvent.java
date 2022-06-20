package com.group4.qrcodepayment.events.event;

import com.group4.qrcodepayment.dto.RegistrationDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Getter
@Setter

public class LoginRegisterEvent extends ApplicationEvent {
    private RegistrationDto registrationDto;

    public LoginRegisterEvent(Object source) {
        super(source);
    }
}
