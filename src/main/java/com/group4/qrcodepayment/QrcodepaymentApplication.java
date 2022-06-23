package com.group4.qrcodepayment;

import com.group4.qrcodepayment.config.JWTConfig;
import com.group4.qrcodepayment.config.SwaggerConfig;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableSwagger2
public class QrcodepaymentApplication {

//    initialize twilio
    @Autowired
    private TwilioConfig config;
//    the postconstruct enable the method to run once the application is initialized

    @PostConstruct
    public void initTwilio(){

        Twilio.init(config.getSid(), config.getAuthToken());
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
