package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.events.publisher.TransactionRecordPublisher;
import com.group4.qrcodepayment.exception.resterrors.BankNotLinkedException;
import com.group4.qrcodepayment.exception.resterrors.CopBankTransactionException;
import com.group4.qrcodepayment.exception.resterrors.TwilioFailedException;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/externals/coop")

public class TransactionController {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private TransactionServiceImpl allTransactionService;
    @Autowired
    private TransactionRecordPublisher recordPublisher;
    @Autowired
    private TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/transfer/result")
    public void getResultTransfer(@RequestBody TransferResponse body ){
        TransactionDto transactionDto = TransactionDto.builder()
                .transactionType("D")
                .date(LocalDateTime.now())
                .transactionAmount(body.getSource().getAmount())
                .transactionRef(body.getMessageReference())
                .destinationAccount("QPAY account")
                .sourceAccount(body.getSource().AccountNumber)
                .build();
        allTransactionService.addTransaction(transactionDto);
//        publish the event to send a message and update the user's QPaY account
        recordPublisher.publishTransactionRecordedEvent(transactionDto.getSourceAccount(),
                transactionDto.getTransactionRef(),"11");
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> sendAmountToAccount(@RequestBody PaymentDetailsFromUser detailsFromUser)
            throws JsonProcessingException, BankNotLinkedException, CopBankTransactionException {
    return transactionService.fundAccount(detailsFromUser);
//         return new CopBankConfiguration().getToken();

    }
}
