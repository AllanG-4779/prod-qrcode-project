package com.group4.qrcodepayment;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class QrcodepaymentApplication {

//    initialize twilio
    @Autowired

    private TwilioConfig config;
    @PostConstruct
    public void initTwilio(){

        Twilio.init(config.getSid(), config.getAuthToken());
    }
    public static void main(String[] args) {
        SpringApplication.run(QrcodepaymentApplication.class, args);
    }

}
