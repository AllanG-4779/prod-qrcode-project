package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.models.Bank;

public interface BankService {
    Bank getBankById(String code);
}