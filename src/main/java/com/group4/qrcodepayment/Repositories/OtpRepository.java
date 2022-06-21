package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.models.OneTimeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OneTimeCode, Long> {
    @Query("SELECT otp from OneTimeCode otp WHERE otp.owner=?1")
    OneTimeCode findByPhoneNotExpired(String phone);
    @Modifying
    @Transactional
    @Query("DELETE FROM OneTimeCode otp WHERE otp.owner=?1 ")
    void deleteByOwner(String owner);
}
