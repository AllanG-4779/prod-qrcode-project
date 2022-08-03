package com.group4.qrcodepayment.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {
    @NotNull(message = "OTP not provided")
    private String code;
    @NotNull(message = "phone number must be provided to verify your code")
    private String phone;
}

