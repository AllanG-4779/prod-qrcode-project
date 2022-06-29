package com.group4.qrcodepayment.externals.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResDto {
    private String access_token;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String expires_in;
}
