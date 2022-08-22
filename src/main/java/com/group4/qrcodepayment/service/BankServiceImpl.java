package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.BankRepo;
import com.group4.qrcodepayment.dto.Bank.BankDto;
import com.group4.qrcodepayment.exception.resterrors.BankNotFoundException;
import com.group4.qrcodepayment.models.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankServiceImpl implements BankService{
    @Autowired
    private BankRepo bankRepo;
    @Override
    public Bank getBankByIpslCode(String code) {
        return bankRepo.findBankByIpslCode(code);
    }

    @Override
    public List<Bank>addNewBank(List<BankDto> bankDto) {

        List<Bank> bank = bankDto.stream().map(
                eachBank->Bank.builder()
                        .ipslCode(eachBank.getIpslCode())
                        .name(eachBank.getName()).build()
        ).collect(Collectors.toList());

        return bankRepo.saveAll(bank);
    }

    @Override
    public int supportBank(String bankName) throws BankNotFoundException {
       return  bankRepo.setSupported(bankName);
    }

    @Override
    public List<Bank> getSupported() {
        return bankRepo.findSupported();
    }
}
