package com.group4.qrcodepayment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "baseurl")
@Configuration

@Data

public class UrlConfig {
    public String baseUrl;

}
