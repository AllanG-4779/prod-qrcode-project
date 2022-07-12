package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.dto.QpayAccountDto;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface QPayAccountService {

//    add account when user registers
    void addNewAccount(QPayAccount account);

    void updateAccount(QPayAccount accountDto);

    QPayAccount getQPayAccount(UserInfo userId);

}
