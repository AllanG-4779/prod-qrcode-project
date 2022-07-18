package com.group4.qrcodepayment.externals.mpesa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUrlReqBody {
    private String ValidationURL;
    private String ConfirmationURL;
    private String ResponseType;
    private Integer ShortCode;
}
