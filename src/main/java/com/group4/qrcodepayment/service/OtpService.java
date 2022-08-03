package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.Auth.OtpDto;
import com.group4.qrcodepayment.models.OneTimeCode;

public interface OtpService {

    void addOtp(OtpDto otpDto);
    OneTimeCode getOtp(String phone);
    OneTimeCode getOtpByCode(String token);
    void deleteOtp(String phone);
    void updateOtp(String token, String owner);
}
