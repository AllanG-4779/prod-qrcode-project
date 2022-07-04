package com.group4.qrcodepayment.externals.coop;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/externals/coop")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/transfer/result")
    public void getResultTransfer(@RequestBody Object body ){
        logger.error("Received reply " + body);
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> sendAmount() throws JsonProcessingException {
    return transactionService.fundAccount();
//         return new CopBankConfiguration().getToken();

    }
}
