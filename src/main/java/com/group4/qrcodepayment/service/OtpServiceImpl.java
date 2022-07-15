package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.OtpRepository;
import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.exception.resterrors.OtpNotGeneratedException;
import com.group4.qrcodepayment.models.OneTimeCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OtpServiceImpl implements OtpService{
    @Autowired
    private OtpRepository otpRepository;
    @Override
    public void addOtp(OtpDto otpDto) {
// clear the otp the number has received before adding a new one;
        otpRepository.deleteByOwner(otpDto.getOwner());
        OneTimeCode code = OneTimeCode
                .builder()
                .expireAt(otpDto.getExpireAt())
                .owner(otpDto.getOwner())
                .issueAt(otpDto.getIssueAt())
                .code(otpDto.getCode())
                .build();
        otpRepository.save(code);

    }

    @Override

    public OneTimeCode getOtp(String phone) {
       OneTimeCode dto = otpRepository.findByPhoneNotExpired(phone);
//       get those codes that hasn't expired
        if(dto == null){
            throw new OtpNotGeneratedException("No OTP has been generated please generate a new one");
        }

        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info(dto.toString());

        return dto;
    }

    @Override
    public OneTimeCode getOtpByCode(String token) {
        return otpRepository.getOneTimeCodeByCode(token);
    }

    @Override
    public void deleteOtp(String phone) {
        otpRepository.deleteByOwner(phone);
    }

    @Override
    public void updateOtp(String token, String owner) {
        otpRepository.updateToken(token, owner);
    }
}
