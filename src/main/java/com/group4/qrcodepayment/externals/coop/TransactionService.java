package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.config.UrlConfig;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import com.group4.qrcodepayment.externals.coop.dto.TransferDto;
import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.util.RandomGenerator;
import com.twilio.rest.api.v2010.account.call.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class TransactionService {
    @Autowired
    private CopBankConfiguration configuration;
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private UrlConfig urlConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());




  public ResponseEntity<?> fundAccount(PaymentDetailsFromUser detailsFromUser) throws JsonProcessingException {
//      Build the source of funds
      SourceDto sourceDto = SourceDto.builder()
              .AccountNumber(accountService.getUserAccountNumber())
              .Amount(detailsFromUser.getAmount())
              .Narration(detailsFromUser.getNarration())
              .TransactionCurrency("KES")
              .build();
//      build the source
      DestinationDto destinationDto =DestinationDto.builder()
              .AccountNumber("54321987654321")

              .BankCode("11")
              .Amount(detailsFromUser.getAmount())
              .TransactionCurrency("KES")
              .Narration(detailsFromUser.getNarration())
              .ReferenceNumber(new RandomGenerator().generateRandom())
              .build();
//      Build the final request Body
      TransferDto transferDto = TransferDto.builder()
              .CallBackUrl(urlConfig.getBaseUrl()+"/externals/coop/transfer/result")
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

      headers.setBearerAuth("Bearer "+token);
      logger.error("GETTING TOKEN FROM FUND METHOD "+CopBankConfiguration.ACCESS_TOKEN);
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
      String requestBody = new ObjectMapper().writeValueAsString(transferDto);

      HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
      logger.error("This is the request to fund " + requestBody);

//      push the request

    ResponseEntity<?> response =   restTemplate.postForEntity(
              "https://openapi-sandbox.co-opbank.co.ke/FundsTransfer/External/A2A/PesaLink/1.0.0",
              entity,
            String.class);
//      if(response.getStatusCode() == HttpStatus.OK){
//          return ResponseEntity.status(200).body(response.getBody());
//      }
     return response;


  }


}
