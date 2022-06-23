package com.group4.qrcodepayment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder

public class JWTokenDto {
    private String token;
    private LocalDateTime iat;
    private LocalDateTime exp;
}
