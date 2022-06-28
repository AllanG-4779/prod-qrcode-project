package com.group4.qrcodepayment.bankingapis.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String scope;
    private Integer expires_in;
}
