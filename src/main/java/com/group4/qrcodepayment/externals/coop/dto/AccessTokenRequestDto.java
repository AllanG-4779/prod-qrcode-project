package com.group4.qrcodepayment.externals.coop.dto;

import lombok.Data;

@Data


public class AccessTokenRequestDto {
    private final String grant_type="password";
    private final String username="allang";
    private final String password = "cnd80751xh";
    private final String scope = "apim:subscribe apim:api_key";

}
