package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.models.QPayAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QPayAccountImpl implements QPayAccountService {
    @Autowired
    private QPayAccountRepo qPayAccountRepo;
    @Override
    public void addNewAccount(QPayAccount account) {
        qPayAccountRepo.save(account);
    }
}
