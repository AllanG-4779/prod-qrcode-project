package com.group4.qrcodepayment;

import com.group4.qrcodepayment.config.SwaggerConfig;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableSwagger2
public class QrcodepaymentApplication {

//    initialize twilio
    @Autowired
    private TwilioConfig config;
//    the post-construct enable the method to run once the application is initialized

    @PostConstruct
    public void initTwilio(){

        Twilio.init(config.getSid(), config.getAuthToken());
    }
   @PostConstruct

   public void init(){
       // Setting Spring Boot SetTimeZone
       TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
   }


    @PostConstruct
    public void initSwagger(){
        new SwaggerConfig().apiDocConfig();
    }

  private static final Logger logger = LoggerFactory.getLogger(QrcodepaymentApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QrcodepaymentApplication.class, args);


    }


}
