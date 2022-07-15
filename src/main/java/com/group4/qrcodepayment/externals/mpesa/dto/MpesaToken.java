package com.group4.qrcodepayment.externals.mpesa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MpesaToken {
    private String expires_in;
    private String access_token;

}
