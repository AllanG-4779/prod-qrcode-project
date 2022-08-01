package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.TransactionTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionTypeImpl implements TransactionType{
    @Autowired
    private TransactionTypeRepo transactionTypeRepo;
    @Override
    public void addTransactionType(com.group4.qrcodepayment.models.TransactionType transactionType) {
        transactionTypeRepo.save(transactionType);
    }
}
