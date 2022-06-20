package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.models.OneTimeCode;

public interface OtpService {

    void addOtp(OtpDto otpDto);
    OneTimeCode getOtp(String phone);
}
