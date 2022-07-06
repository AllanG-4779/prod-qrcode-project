package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.models.QPayAccount;

public interface QPayAccountService {

//    add account when user registers
    void addNewAccount(QPayAccount account);

}
