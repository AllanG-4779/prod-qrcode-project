package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.OtpRepository;
import com.group4.qrcodepayment.dto.OtpDto;
import com.group4.qrcodepayment.models.OneTimeCode;
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
        OneTimeCode code = OneTimeCode
                .builder()
                .expiry(otpDto.getExpiry())
                .owner(otpDto.getOwner())
                .issue(otpDto.getIssue())
                .code(otpDto.getCode())
                .build();
        otpRepository.save(code);

    }

    @Override
    public OneTimeCode getOtp(String phone) {
       List<OneTimeCode> dto = otpRepository.findByPhoneNotExpired(phone);
//       get those codes that hasn't expired
        if(dto ==null){
            throw new RuntimeException("In correct OTP");
        }
       List<OneTimeCode> code =  dto.stream().filter(
               x->
                       LocalDateTime.now().compareTo(x.getIssue().plusMinutes(x.getExpiry())) > 0

       ).collect(Collectors.toList());

        return code.get(0);
    }
}
