package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepoInt extends JpaRepository<UserInfo, Long> {
    @Query("SELECT user FROM UserInfo user where user.email=?1")
    UserInfo findUserByEmail(String email);

   @Query("SELECT user FROM UserInfo user WHERE user.phone=?1")
   UserInfo findByUsername(String username);

   @Query("SELECT user FROM UserInfo user WHERE user.email=?1 OR user.phone=?1")
   UserInfo findUserByPhoneOREmail(String usernameOrEmail);

   Optional<UserInfo> findUserInfoByPhone(String phone);

   @Query("SELECT user FROM UserInfo user INNER JOIN Account acc ON user.userId = acc.userId WHERE acc.accountNumber =?1 AND acc.bankId=?2")
   UserInfo findUserByBankIdAndAccountNumber(String acc_number, String bankId);



   @Modifying
   @Transactional
   @Query("UPDATE UserInfo  useraccount SET useraccount.isConfirmed=true WHERE useraccount.phone=?1")
    void verifyAccount(String phone);
}
