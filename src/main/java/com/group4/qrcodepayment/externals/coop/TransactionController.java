package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/coop/transfer/result")
    public void getResultTransfer(@RequestBody Object body ){
        logger.error("Received reply " + body);
    }
    @PostMapping("/coop/transfer")
    public ResponseEntity<?> sendAmount() throws JsonProcessingException {
    return transactionService.fundAccount();
//         return new CopBankConfiguration().getToken();

    }
}
