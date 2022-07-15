package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.models.Transactions;


public interface Transactionservice {
    void addTransaction(TransactionDto transactionDto) throws TransactionNotFoundException;
    Transactions getTransactionById(String id);

}
