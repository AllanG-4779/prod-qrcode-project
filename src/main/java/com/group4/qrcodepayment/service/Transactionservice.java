package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.models.Transactions;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;


public interface Transactionservice {
    void addTransaction(TransactionDto transactionDto) throws TransactionNotFoundException;
    Transactions getTransactionById(String id);

    void completeTransaction(Transactions transaction);

}
