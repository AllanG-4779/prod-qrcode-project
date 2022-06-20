package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.models.OneTimeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OneTimeCode, Long> {
    @Query("SELECT otp from OneTimeCode otp WHERE otp.owner=?1")
    List<OneTimeCode> findByPhoneNotExpired(String phone);
}
