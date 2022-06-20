package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepoInt extends JpaRepository<UserInfo, Long> {
    @Query("SELECT user FROM UserInfo user where user.email=?1")
    UserInfo findUserByEmail(String email);

   @Query("SELECT user FROM UserInfo user WHERE user.phone=?1")
   UserInfo findByUsername(String username);

   @Query("SELECT user FROM UserInfo user WHERE user.email=?1 OR user.phone=?1")
   UserInfo findUserByPhoneOREmail(String usernameOrEmail);
}
