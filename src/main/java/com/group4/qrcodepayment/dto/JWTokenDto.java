package com.group4.qrcodepayment.dto;

import lombok.Builder;
import lombok.Data;



@Data
@Builder

public class JWTokenDto {
    private String token;
    private String user_id;
}
