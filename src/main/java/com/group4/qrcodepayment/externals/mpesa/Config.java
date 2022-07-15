package com.group4.qrcodepayment.externals.mpesa;

import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


@Configuration
@Data
@ConfigurationProperties(prefix = "mpesa")
public class Config {
    private String consumerKey;
    private String consumerSecret;

//    get the token

    public MpesaToken getToken(){

        RestTemplate templete = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setBasicAuth(consumerKey, consumerSecret);

//Construct the request
        HttpEntity<?> request= new HttpEntity<>(null, headers);
//      issue request
        ResponseEntity<MpesaToken> tokenResponse = templete.exchange("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials",
                HttpMethod.GET, request, MpesaToken.class);

        return tokenResponse.getBody();







    }

}
