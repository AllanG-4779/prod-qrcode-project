package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.BankDto;
import com.group4.qrcodepayment.exception.resterrors.BankNotFoundException;
import com.group4.qrcodepayment.models.Bank;


import java.util.List;

public interface BankService {

    Bank getBankByIpslCode(String code);
    List<Bank> addNewBank(List<BankDto> bankDto);
    int supportBank(String bankName) throws BankNotFoundException;
}
