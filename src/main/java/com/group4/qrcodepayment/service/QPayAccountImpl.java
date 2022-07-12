package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.dto.QpayAccountDto;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class QPayAccountImpl implements QPayAccountService {

    @Autowired
    private QPayAccountRepo qPayAccountRepo;
    @Override
    public void addNewAccount(QPayAccount account) {
        qPayAccountRepo.save(account);
    }
    @Override
    @Transactional
    public void updateAccount(QPayAccount qPayAccount) {


        qPayAccountRepo.save(qPayAccount);





    }

    @Override
    public QPayAccount getQPayAccount(UserInfo userId) {
        return qPayAccountRepo.findQPayAccountByUserId(userId);
    }
}
