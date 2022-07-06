package com.group4.qrcodepayment.customresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponse {
    private Integer code;
    private String message;
    private String reason;
}
