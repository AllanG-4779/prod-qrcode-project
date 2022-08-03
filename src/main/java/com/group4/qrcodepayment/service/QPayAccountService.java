package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;

public interface QPayAccountService {

//    add account when user registers
    void addNewAccount(QPayAccount account);

    void updateAccount(Integer amount, UserInfo user);

    QPayAccount getQPayAccount(UserInfo userId);

}
