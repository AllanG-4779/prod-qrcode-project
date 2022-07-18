package com.group4.qrcodepayment.externals.mpesa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import com.group4.qrcodepayment.externals.mpesa.dto.RegisterUrlReqBody;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
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
//Register URL
    public Object registerUrl() throws JsonProcessingException {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
//      pass the access token
      headers.setBearerAuth(getToken().getAccess_token());
      headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));

        RegisterUrlReqBody registerUrlReqBody = RegisterUrlReqBody.builder()
                .ConfirmationURL("localhost:8001/mpesa/confirmation")
                .ValidationURL("localhost:8001/mpesa/validation")
                .ResponseType("Canceled")
                .ShortCode(600999)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(registerUrlReqBody);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug(reqBody);



      HttpEntity<?> request = new HttpEntity<>(reqBody, headers);
      ResponseEntity<?> res = restTemplate.exchange("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl",
              HttpMethod.GET,request, Object.class
              );
            return res.getBody();

    }
}
