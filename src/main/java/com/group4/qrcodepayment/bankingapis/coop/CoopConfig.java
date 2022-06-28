package com.group4.qrcodepayment.bankingapis.coop;

import com.group4.qrcodepayment.bankingapis.coop.dto.TokenDto;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Random;

@Data
@Service
public class CoopConfig {
    private final String  KCB_SECRET = "97U_atPcLRGhkhoCq4g06JzcVWsa";
    private final String KCB_CONSUMER = "gKrUCsYMM82_e9buFIaYIHBQ5T0a";


    private  RestTemplate restTemplate;
    Logger log = LoggerFactory.getLogger(this.getClass());

//    get access token


    public String getToken() throws URISyntaxException {
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

//        this is the basic auth used to get the token

        String encodedBytes = Base64.getEncoder().encodeToString((KCB_CONSUMER+":"+KCB_SECRET).getBytes());

        httpHeaders.setBasicAuth(encodedBytes);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        set the body


//        build the request
        String formData = "username=allang&password=cnd80751xh&grant_type=password&scope=bim:api";

        HttpEntity<?> request = new HttpEntity<>(formData, httpHeaders);

        ResponseEntity<?> res  = restTemplate.postForEntity("https://openapi-sandbox.co-opbank.co.ke/token",
              request, TokenDto.class);

        TokenDto tokenDto;
        tokenDto = (TokenDto) res.getBody();
    assert tokenDto != null;
    log.info(tokenDto.getAccess_token());

    return tokenDto.getAccess_token();
    }







   public ResponseEntity<?> fundAccount() throws JSONException, URISyntaxException {
    restTemplate = new RestTemplate();
// set the headers to APPLICATION/JSON
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_JSON);
    JSONObject object = new JSONObject();
    object.put("MessageReference", "KTDSMN4524FLS");
    object.put("CallbackUrl",  "http://localhost:8001/secure/coop/res");
    JSONObject source = new JSONObject();
    source.put("AccountNumber", "36001873000");
    source.put("Amount", "800");
    source.put("TranferCurrency", "KES");
    source.put("Naration", "Bill payment service");
    object.put("Source", source);
    JSONObject destination = new JSONObject();

    destination.put("ReferenceNumber", "SCT2218948930");
    destination.put("AccountNumber", "54321987654321");
    destination.put("Amount", "800");
    destination.put("TransactionCurrency", "KES");
    destination.put("Narration", "Being the bill payment for the results");

    object.put("Destination", destination);


//build the request
    HttpEntity<?> request = new HttpEntity<>(object.toString(), headers);
    log.debug("FROM FUND ACCOUNT "+ getToken());
    headers.setBearerAuth(getToken());

    ResponseEntity<?> res= restTemplate.postForEntity("https://developer.co-opbank.co.ke/FundsTransfer/External/A2A/PesaLink/1.0.0",
            request,String.class);

    log.error("Hello " + res+" ");

    return res;


}

}
