package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import com.group4.qrcodepayment.externals.coop.dto.TransferDto;
import com.group4.qrcodepayment.externals.coop.dto.TransferResDto;
import com.group4.qrcodepayment.util.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class TransactionService {
    @Autowired
    private CopBankConfiguration configuration;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    Transfer body

  private final TransferDto transferDto = TransferDto.builder()
          .MessageReference(new RandomGenerator().generateRandom())
          .CallBackUrl("https://marabal.herokuapp.com/externals/coop/transfer/result")
          .Source(
                SourceDto
                        .builder()
                        .AccountNumber("36001873000")
                        .Amount("777")
                        .TransactionCurrency("KES")
                        .Narration("Funding My QPay Account")
                        .build()
        )
        .Destinations(
                new DestinationDto[]{
                        DestinationDto
                                .builder()
                                .ReferenceNumber(new RandomGenerator().generateRandom())
                                .AccountNumber("54321987654321")
                                .BankCode("11")
                                .TransactionCurrency("KES")
                                .Amount("777")
                                .Narration("Funding my Account")
                                .build()
                }
        )
        .build();

  public ResponseEntity<?> fundAccount() throws JsonProcessingException {
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
