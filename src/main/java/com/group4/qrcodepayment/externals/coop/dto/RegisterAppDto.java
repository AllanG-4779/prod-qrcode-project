package com.group4.qrcodepayment.externals.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAppDto {

    private String callbackUrl;
    private String clientName;
    private String owner;
    private String grantType;
    private Boolean saasApp;
}
