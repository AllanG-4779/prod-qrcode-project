package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.exception.resterrors.TwilioFailedException;
import com.group4.qrcodepayment.externals.coop.dto.PaymentDetailsFromUser;
import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
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

@RestController
@RequestMapping("/externals/coop")

public class TransactionController {
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/transfer/result")
    public void getResultTransfer(@RequestBody TransferResponse body ){

//        logger.info(body.messageReference + " Confirmed at "+body.messageDateTime+". "+body.destinations.get(0).TransactionCurrency+ body.source.Amount +" transferred" +
//                " to Account Your QPAY account" );0
//        Send an SMS
    logger.error(body.toString());
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> sendAmountToAccount(@RequestBody PaymentDetailsFromUser detailsFromUser)
            throws JsonProcessingException {
    return transactionService.fundAccount(detailsFromUser);
//         return new CopBankConfiguration().getToken();

    }
}
