package com.group4.qrcodepayment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDto {
    private String phoneOrEmail;
    private String password;
}
