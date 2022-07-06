package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.BankRepo;
import com.group4.qrcodepayment.models.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService{
    @Autowired
    private BankRepo bankRepo;
    @Override
    public Bank getBankById(String code) {
        return bankRepo.findBankByipslCode(code);
    }
}
