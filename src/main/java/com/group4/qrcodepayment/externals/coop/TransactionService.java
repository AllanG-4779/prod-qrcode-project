package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.config.UrlConfig;
import com.group4.qrcodepayment.exception.resterrors.BankNotLinkedException;
import com.group4.qrcodepayment.exception.resterrors.CopBankTransactionException;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import com.group4.qrcodepayment.externals.coop.dto.TransferDto;
import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.util.RandomGenerator;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.call.Payment;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

@Service
public class TransactionService {
    @Autowired
    private CopBankConfiguration configuration;
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private UrlConfig urlConfig;
    @Autowired
    private TwilioConfig twilioConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());




  public ResponseEntity<?> fundAccount(PaymentDetailsFromUser detailsFromUser) throws JsonProcessingException, BankNotLinkedException, CopBankTransactionException {
//      who is logged in
      String phoneNumber = null;
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
          phoneNumber = authentication.getName();
      }
//      Build the source of funds
      SourceDto sourceDto = SourceDto.builder()
              .AccountNumber(accountService.getUserAccountNumber())
              .Amount(detailsFromUser.getAmount())
              .Narration(detailsFromUser.getNarration())
              .TransactionCurrency("KES")
              .build();
//      build the source
      DestinationDto destinationDto = DestinationDto.builder()
              .AccountNumber("54321987654321")

              .BankCode("11")
              .Amount(detailsFromUser.getAmount())
              .TransactionCurrency("KES")
              .Narration(detailsFromUser.getNarration())
              .ReferenceNumber(new RandomGenerator().generateRandom())
              .build();
//      Build the final request Body
      TransferDto transferDto = TransferDto.builder()
              .CallBackUrl(urlConfig.getBaseUrl() + "/externals/coop/transfer/result")
              .MessageReference(new RandomGenerator().generateRandom())
              .Source(sourceDto)
              .Destinations(
                      new DestinationDto[]{
                              destinationDto
                      }
              )
              .build();
      RestTemplate restTemplate = new RestTemplate();
//      Call the method to get token
      String token = configuration.getToken();
      HttpHeaders headers = new HttpHeaders();

      headers.setBearerAuth("Bearer " + token);
      logger.error("GETTING TOKEN FROM FUND METHOD " + CopBankConfiguration.ACCESS_TOKEN);
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      String requestBody = new ObjectMapper().writeValueAsString(transferDto);

      HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
      logger.error("This is the request to fund " + requestBody);

//      push the request
      ResponseEntity<?> response  = null;
try{
      response = restTemplate.postForEntity(
              "https://openapi-sandbox.co-opbank.co.ke/FundsTransfer/External/A2A/PesaLink/1.0.0",
              entity,
              String.class);


  }catch(Exception e){
          Message.creator(
                  new PhoneNumber("+254"+phoneNumber),
                  new PhoneNumber(twilioConfig.getTrialNumber()),
                  "\nDear QPay user, We are sorry to let you know that your request to fund your QPay account was not successful.\n" +
                          "Please try again in a little bit or contact us if this issue persist\n" +
                          "Thank you for understanding."
          ).create();
          throw new CopBankTransactionException(e.getMessage());

      }
      assert response != null;
      if(Objects.requireNonNull(response.getBody()).toString().contains("REQUEST ACCEPTED FOR PROCESSING")){
          Message.creator(
                  new PhoneNumber("+254"+phoneNumber),
                  new PhoneNumber(twilioConfig.getTrialNumber()),
                  "Dear QPay user, You have initiated a transaction to fund your Account from your " +
                          "Coperative Bank Account.\n" +
                          "Please await Confirmation from your Bank.\n" +
                          "Thank you!!"
          ).create();
      }

    return response;



  }


}
