package com.group4.qrcodepayment.externals.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterUrlReqBody {
    @JsonProperty("ValidationURL")
    private String ValidationURL;
    @JsonProperty("ConfirmationURL")
    private String ConfirmationURL;
    @JsonProperty("ResponseType")
    private String ResponseType;
    @JsonProperty("ShortCode")
    private String ShortCode;
}
