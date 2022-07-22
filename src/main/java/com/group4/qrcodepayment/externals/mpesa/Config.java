package com.group4.qrcodepayment.externals.mpesa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.config.UrlConfig;
import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.TransactionFailedException;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.externals.mpesa.dto.MpesaFundAccount;
import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import com.group4.qrcodepayment.externals.mpesa.dto.RegisterUrlReqBody;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.CustomUserDetails;
import com.group4.qrcodepayment.security.CustomUserDetailsService;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationImpl;
import com.group4.qrcodepayment.util.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


@Configuration
@Data

@ConfigurationProperties(prefix = "mpesa")
public class Config {
    private String consumerKey;
    private String consumerSecret;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UrlConfig  baseUrl;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private UserRegistrationImpl userRegistration;


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
     Calendar cal = Calendar.getInstance();

      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
//      pass the access token
      headers.set("Authorization","Bearer "+getToken().getAccess_token());
      headers.set("Content-Type","application/json");

        RegisterUrlReqBody registerUrlReqBody = RegisterUrlReqBody.builder()
                .ConfirmationURL("https://marabal.herokuapp.com/v1/confirmation")
                .ValidationURL("https://marabal.herokuapp.com/v1/validation")
                .ResponseType("Completed")
                .ShortCode("174379")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String reqBody = mapper.writeValueAsString(registerUrlReqBody);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug(reqBody);



      HttpEntity<?> request = new HttpEntity<>(reqBody, headers);
      ResponseEntity<?> res = restTemplate.postForEntity("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl",
             request, Object.class
              );

        return res.getBody();
    }

//    invoke an STK push
    public Object fundAccountViaMpesa(String amount) throws JsonProcessingException, AuthenticationNotFoundException, TransactionNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken)){
            throw new AuthenticationNotFoundException("To use MPESA please sign in first");
        }
        UserDetails user = userDetailsService.loadUserByUsername(auth.getName());
        UserInfo transactingObject = userRegistration.findUserByPhone(user.getUsername());
//        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        Date myDate = new Date();
        String timeStamp = format.format(myDate);
        String passStr = "174379"+"bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"+timeStamp;

        String password = Base64.getEncoder().encodeToString(passStr.getBytes());
        MpesaFundAccount fundAccountBody = MpesaFundAccount
                .builder()
                .BusinessShortCode("174379")
                .Password(password)
                .Amount(amount)
                .CallBackURL(baseUrl.getBaseUrl()+"/v1/confirmation")
                .PartyA("254"+user.getUsername())
                .PartyB("174379")
                .PhoneNumber("254796407365")
                .Timestamp(timeStamp)
                .AccountReference("TMS80383TX12")
                .TransactionDesc("Paying QPay")
                .TransactionType("CustomerPayBillOnline")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getToken().getAccess_token());
        HttpEntity<?> httpRequest = new HttpEntity<>(new ObjectMapper()
                .writeValueAsString(fundAccountBody),headers);

//        Actual request

        RestTemplate template = new RestTemplate();


            ResponseEntity<?> res =  template.postForEntity("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest", httpRequest, LinkedHashMap.class);
            TransactionDto transactionDto = TransactionDto
                    .builder()
                    .date(LocalDateTime.now())

                    .transactionType("D")
                    .transactionAmount(fundAccountBody.getAmount())
                    .transactionRef(((LinkedHashMap<?,?>) res.getBody()).get("CheckoutRequestID").toString())
                    .destinationAccount("QPay ACCOUNT")
                    .sourceAccount("MPESA ACCOUNT")
                    .status("Pending")
                    .userId(transactingObject)
                    .build();

            transactionService.addTransaction(transactionDto);

            return  res.getBody();




    }
}
