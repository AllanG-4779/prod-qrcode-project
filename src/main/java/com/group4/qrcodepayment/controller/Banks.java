package com.group4.qrcodepayment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.qrcodepayment.dto.BankDto;
import com.group4.qrcodepayment.exception.resterrors.BankNotFoundException;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.service.BankServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class Banks {

    @Autowired
    public BankServiceImpl bankService;
    @PostMapping("/add")
    public List<Bank> addBank(@RequestBody Map<String, List<@Valid BankDto>> bank){
        List <BankDto> banks = bank.get("banks");
        return bankService.addNewBank(banks);
    }

    @PostMapping("/support")
    public ResponseEntity<?> setSupported(@RequestParam @NotBlank @NotNull String name){
       int result =  bankService.supportBank(name);
       Map<String, String> map = new LinkedHashMap<>();
       map.put("message", name+ " is now supported");
       map.put("code", "200");

       if (result <= 0){
           throw new BankNotFoundException("Double check the name of the bank");
       }else{
           return ResponseEntity.status(200).body(
                  map
           );
       }

    }


}
