package com.group4.qrcodepayment.events.handler;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.events.event.LoginRegisterEvent;

import com.group4.qrcodepayment.exception.resterrors.TwilioFailedException;
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
    private static final int OTP_EXPIRE = 1;

@EventListener
@Async
    public void sendOTP(LoginRegisterEvent loginRegisterEvent){


 //     send an sms or notification
    String OTP = getOTP();
try{
    Message message = Message.creator(
            new PhoneNumber("+254"+loginRegisterEvent.getPhoneNumber()),
            new PhoneNumber(twilioConfig.getTrialNumber()),
            "\nYour OTP for QPay is: " +
                    OTP
    ).create();

}
catch(com.twilio.exception.ApiException e){
    throw new TwilioFailedException("QPay is using a trial Version of Twilio," +
            " Please ask admin to include your number in list of allowed numbers");
}


    //save the OTP to the database
    OtpDto code = OtpDto.builder()
            .code(OTP)
            .issueAt(LocalDateTime.now())
            .owner(loginRegisterEvent.getPhoneNumber())
            .expireAt(LocalDateTime.now().plusMinutes(OTP_EXPIRE))
            .build();
    otpService.addOtp(code);




    }

    public String getOTP(){
        Logger logger = LoggerFactory.getLogger(LoginRegisterEventHandler.class);
        logger.info("Initiating the OTP generation");
        String OTP = new DecimalFormat("0000").format(new Random().nextInt(9999));
       logger.info("Successfully generated an OTP ");
       return OTP;
    }
}
