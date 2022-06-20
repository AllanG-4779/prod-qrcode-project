package com.group4.qrcodepayment.events.handler;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.events.event.LoginRegisterEvent;

import com.group4.qrcodepayment.models.OneTimeCode;
import com.group4.qrcodepayment.service.OtpServiceImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.event.TreeWillExpandListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Random;

@Component

public class LoginRegisterEventHandler {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private OtpServiceImpl otpService;

@EventListener
@Async
    public void sendOTP(LoginRegisterEvent loginRegisterEvent){

 //     send an sms or notification
    String OTP = getOTP();

    Message message = Message.creator(
            new PhoneNumber("+254"+loginRegisterEvent.getRegistrationDto().getPhone()),
            new PhoneNumber(twilioConfig.getTrialNumber()),
            "\nHi! This number has been used to register on our site!\nTo confirm the same, Please " +
                    "enter this OTP\n" +
                    OTP
    ).create();

    //save the OTP to the database
    OtpDto code = OtpDto.builder()
            .code(OTP)
            .issue(LocalDateTime.now())
            .owner(loginRegisterEvent.getRegistrationDto().getPhone())
            .expiry(1)
            .build();
    otpService.addOtp(code);




    }

    public String getOTP(){
        Logger logger = LoggerFactory.getLogger(LoginRegisterEventHandler.class);
        logger.info("Initiating the OTP generation");
        String OTP = new DecimalFormat("0000000").format(new Random().nextInt(999999));
       logger.info("Successfully generated an OTP ");
       return OTP;
    }
}
