package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface QPayAccountRepo extends JpaRepository<QPayAccount, String> {
        QPayAccount findQPayAccountByUserId(UserInfo userId);
        @Transactional
        @Query("UPDATE QPayAccount qp SET qp.balance = qp.balance+?1")
        QPayAccount updateBalance(Integer amount);

}
