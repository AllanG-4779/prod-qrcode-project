package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.dto.Bank.BankDto;
import com.group4.qrcodepayment.dto.SuccessMessage;
import com.group4.qrcodepayment.dto.Accounts.TransactionTypeDto;
import com.group4.qrcodepayment.exception.resterrors.BankNotFoundException;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.TransactionType;
import com.group4.qrcodepayment.service.BankServiceImpl;
import com.group4.qrcodepayment.service.TransactionTypeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/bank")
public class Banks {

    @Autowired
    public BankServiceImpl bankService;
    @Autowired
    public TransactionTypeImpl transactionTypeService;
    @PostMapping("/add")
    public List<Bank> addBank(@RequestBody Map<String, List<@Valid BankDto>> bank){
        List <BankDto> banks = bank.get("banks");
        return bankService.addNewBank(banks);
    }

    @PostMapping("/support")
    public ResponseEntity<?> setSupported(@RequestParam @NotBlank @NotNull String name){
       int result =  bankService.supportBank(name);
      try{
          Bank bank = bankService.getBankByIpslCode(name);
          Map<String, String> map = new LinkedHashMap<>();
          map.put("message", "QPay users can now link their "+ bank.getName() +" account to transact" );
          map.put("code", "200");

          return ResponseEntity.status(200).body(
                  map
          );
       }
      catch(NullPointerException ex){
          throw new BankNotFoundException("Double check the name of the bank");

       }

    }
//    Activate Transaction types i.e Deposit, Widthdrawal, Transfer

    @PostMapping("/transaction-type/add")
    public ResponseEntity<?> addTransactionType(@RequestBody TransactionTypeDto transactionTypeDto){

        TransactionType transactionType = TransactionType.builder()
                .transactionId(transactionTypeDto.getTransactionId())
                .transactionName(transactionTypeDto.getTransactionName())
                .build();
        try{
            transactionTypeService.addTransactionType(transactionType);
        }catch(Exception e ){
            throw new RuntimeException("Something went wrong");
        }


                return ResponseEntity.status(200).body(SuccessMessage.builder()
                        .code(200)
                        .message("Action completed successfully")
                        .localDateTime(LocalDateTime.now())

                .build());
    }
    @GetMapping("/supported")
    public List<Bank> getAllSupportedBanks(){
        return bankService.getSupported();
    }

}
