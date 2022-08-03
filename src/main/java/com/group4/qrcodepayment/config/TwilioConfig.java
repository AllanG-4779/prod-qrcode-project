package com.group4.qrcodepayment.config;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "twilio")
@Configuration
public class TwilioConfig {
    private String sid;
    private String authToken;
    private String trialNumber;

    public void sendSMS(String From, String phone, String body){
        Message.creator(
                new PhoneNumber("+254"+phone),
                new PhoneNumber(From),
                body
        ).create();
    }
}
