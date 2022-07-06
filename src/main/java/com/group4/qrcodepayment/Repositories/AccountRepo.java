package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.models.AccountId;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, AccountId> {
    Account findAccountByUserIdAndBankId(UserInfo user, Bank bankId);
}
