package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group4.qrcodepayment.events.publisher.TransactionRecordPublisher;
import com.group4.qrcodepayment.exception.resterrors.BankLinkedException;
import com.group4.qrcodepayment.exception.resterrors.CopBankTransactionException;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/externals/coop")

public class TransactionController {

    @Autowired
    private TransactionServiceImpl allTransactionService;
    @Autowired
    private TransactionRecordPublisher recordPublisher;
    @Autowired
    private TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/transfer/result")
    public void getResultTransfer(@RequestBody TransferResponse body ) {


//        publish the event to send a message and update the user's QPaY account
        recordPublisher.publishTransactionRecordedEvent(body);
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> sendAmountToAccount(@RequestBody PaymentDetailsFromUser detailsFromUser)
            throws JsonProcessingException, BankLinkedException, CopBankTransactionException, TransactionNotFoundException {
    return transactionService.fundAccount(detailsFromUser);
//         return new CopBankConfiguration().getToken();

    }
}
