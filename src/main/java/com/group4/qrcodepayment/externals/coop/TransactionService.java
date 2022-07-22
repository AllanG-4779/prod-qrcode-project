package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.config.UrlConfig;
import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.exception.resterrors.BankLinkedException;
import com.group4.qrcodepayment.exception.resterrors.CopBankTransactionException;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import com.group4.qrcodepayment.externals.coop.dto.TransferDto;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationImpl;
import com.group4.qrcodepayment.util.RandomGenerator;
import com.twilio.rest.api.v2010.account.Message;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Locale;
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
    TransactionServiceImpl recordTransactionService;
    @Autowired
    UserRegistrationImpl userRegistration;
    @Autowired
    private TwilioConfig twilioConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());




  public ResponseEntity<?> fundAccount(PaymentDetailsFromUser detailsFromUser)
          throws JsonProcessingException, BankLinkedException,
          CopBankTransactionException, TransactionNotFoundException {
//      who is logged in
      UserInfo user = null;
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (!(authentication == null || authentication instanceof AnonymousAuthenticationToken)) {
          user = userRegistration.findUserByPhone(authentication.getName());
      }
//      Build the source of funds
      SourceDto sourceDto = SourceDto.builder()
              .AccountNumber(accountService.getUserAccountNumber())
              .Amount(detailsFromUser.getAmount())
              .Narration("FUNDING QPAY ACCOUNT")
              .TransactionCurrency("KES")
              .build();
//      build the destination

     String  TransferReference = new RandomGenerator().generateRandom();
      DestinationDto destinationDto = DestinationDto.builder()
              .AccountNumber("54321987654321")
              .BankCode("11")
              .Amount(detailsFromUser.getAmount())
              .TransactionCurrency("KES")
              .Narration("QPAY FUNDING")
              .ReferenceNumber(TransferReference+"_B")
              .build();
//      Build the final request Body
      TransferDto transferDto = TransferDto.builder()
              .CallBackUrl(urlConfig.getBaseUrl() + "/externals/coop/transfer/result")
              .MessageReference(TransferReference)
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
      ResponseEntity<?> response;
try{
      response = restTemplate.postForEntity(
              "https://openapi-sandbox.co-opbank.co.ke/FundsTransfer/External/A2A/PesaLink/1.0.0",
              entity,
              String.class);


  }catch(Exception e){
    assert user != null;
    Message.creator(
                  new PhoneNumber("+254"+user.getPhone()),
                  new PhoneNumber(twilioConfig.getTrialNumber()),
                  "\nDear QPay user, We are sorry to let you know that your request to fund your QPay account was not successful.\n" +
                          "Please try again in a little bit or contact us if this issue persist\n" +
                          "Thank you for understanding."
          ).create();
          throw new CopBankTransactionException(e.getMessage());

      }

      if(Objects.requireNonNull(response.getBody()).toString().contains("REQUEST ACCEPTED FOR PROCESSING")){
          assert user != null;
          Message.creator(
                  new PhoneNumber("+254"+user.getPhone()),
                  new PhoneNumber(twilioConfig.getTrialNumber()),
                  "Dear QPay user, You have initiated a transaction to fund your Account from your " +
                          "Coperative Bank Account.\n" +
                          "Please await Confirmation from your Bank.\n" +
                          "Transaction Ref: "+TransferReference+"."
          ).create();
//          save the transaction
          TransactionDto transactionDto = TransactionDto
                  .builder()
                  .sourceAccount(sourceDto.getAccountNumber())
                  .destinationAccount(destinationDto.getAccountNumber())
                  .transactionRef(TransferReference)
                  .transactionAmount(sourceDto.getAmount())
                  .transactionType("D")
                  .date(LocalDateTime.now())
                  .userId(user)
                  .build();
          recordTransactionService.addTransaction(transactionDto);

      }

    return response;



  }


}
