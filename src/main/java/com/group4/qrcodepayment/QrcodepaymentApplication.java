package com.group4.qrcodepayment;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
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
    public static void main(String[] args) {
        SpringApplication.run(QrcodepaymentApplication.class, args);
    }
    @Bean
    public Docket swaggerAnnotation(){
     return new Docket(DocumentationType.SWAGGER_2)
             .select()
             .paths(PathSelectors.ant("/insights"))

             .build();
    }


}
