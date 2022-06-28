package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.bankingapis.coop.CoopConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/secure/")
public class BankControllers {
    @Autowired
 private CoopConfig config;
//    fund account from cooperative bank account
    @PostMapping("/coop/fund")
    public ResponseEntity<?> addCashToAccount() throws JSONException, URISyntaxException {
        return config.fundAccount();
    }

    @GetMapping("/coop/res")
    public void transactionRes(@RequestBody String req){
     System.out.println(req);
    }
}
