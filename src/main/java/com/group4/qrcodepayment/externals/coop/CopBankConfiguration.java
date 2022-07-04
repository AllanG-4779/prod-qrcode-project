package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.externals.coop.dto.AccessTokenRequestDto;
import com.group4.qrcodepayment.externals.coop.dto.AccessTokenResDto;
import com.group4.qrcodepayment.externals.coop.dto.RegisterAppDto;
import com.group4.qrcodepayment.externals.coop.dto.RegisterAppResDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Collections;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CopBankConfiguration {
    private static String SECRET;
    private static String CLIENT_ID;
    public static String ACCESS_TOKEN;
    private RestTemplate restTemplate;
    private Logger log = LoggerFactory.getLogger(this.getClass());


//    Register the app by sending an internal request
    public void registerApp() throws JsonProcessingException {
//  initialize the template
        restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

//  set the headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("allang","cnd80751xh");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//  Construct the bod
        RegisterAppDto data = RegisterAppDto
                .builder()
                .callbackUrl("https://7bbc-197-232-61-192.ngrok.io/coop/res")
                .clientName("SendtoAccount")
                .grantType("client_credentials password refresh_token")
                .saasApp(true)
                .owner("allang")
                .build();

//  Build the request
        HttpEntity<?> request = new HttpEntity<>(objectMapper.writeValueAsString(data), httpHeaders);
//        send the request
        ResponseEntity<?> responseEntity = restTemplate.postForEntity("https://developer.co-opbank.co.ke/client-registration/v0.17/register",
                request, RegisterAppResDto.class);

        
        RegisterAppResDto registerAppResDto = (RegisterAppResDto) responseEntity.getBody();
        assert registerAppResDto != null;
        log.error(registerAppResDto.getAppOwner());

//        set the secrets
        CopBankConfiguration.CLIENT_ID= registerAppResDto.getClientId();
        CopBankConfiguration.SECRET = registerAppResDto.getClientSecret();
        
        log.info("CLIENT SECRET: "+ registerAppResDto.getClientSecret());
        log.info("CLIENT ID "+ registerAppResDto.getClientId());


    }

    public String getToken() throws JsonProcessingException {
        restTemplate = new RestTemplate();
//        get the headers right
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        SET CREDENTIALS by calling the method responsible
        registerApp();
        log.error("After sending the request  the secret is "+ SECRET );
        String encodedCredentials = Base64.getEncoder().encodeToString((CLIENT_ID+":"+SECRET).getBytes());
        headers.setBasicAuth(encodedCredentials);
//      Body of the requst
        AccessTokenRequestDto access = new AccessTokenRequestDto();
        String body = "grant_type="+access.getGrant_type()+"&username="+
                access.getUsername()+"&password="+access.getPassword()+"&scope="+access.getScope();

        HttpEntity<?> request = new HttpEntity<>(body, headers);
        ResponseEntity<?> res =  restTemplate.postForEntity("https://openapi-sandbox.co-opbank.co.ke/token",request, AccessTokenResDto.class);

        AccessTokenResDto token = (AccessTokenResDto) res.getBody();
        assert token != null;
        log.error("ACCESS TOKEN FROM GET TOKEN METHOD "+ token.getAccess_token());
        ACCESS_TOKEN = token.getAccess_token();
        return token.getAccess_token();
    }
}
