package com.group4.qrcodepayment.externals.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAppResDto {

    private String clientId;
    private String clientName;
    private String callBackUrl;
    private String clientSecret;
    private Boolean isSaasApplication;
    private String appOwner;
    private String jsonString;
    private String jsonAppAttribute;
    private String tokenType;

}
