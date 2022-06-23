package com.group4.qrcodepayment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket apiDocConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/auth/**"))
                .apis(RequestHandlerSelectors.basePackage("com.group4.qrcodepayment"))
                .build()
                .apiInfo(apiInfo());

    }
    @Bean
    public ApiInfo apiInfo(){
        return new ApiInfo("QrcodePayment API",
                "This is a backend for serving requests from the web portal and the mobile",
                "1.0",
                "This is a private API please get consent before using it",
                "hello",
                "Apache Licence",
                "www.aparche.org");
    }

}
