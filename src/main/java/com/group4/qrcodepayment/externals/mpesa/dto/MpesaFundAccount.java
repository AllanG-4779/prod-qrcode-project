package com.group4.qrcodepayment.externals.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MpesaFundAccount {
   @JsonProperty("BusinessShortCode")
    private String BusinessShortCode;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("Timestamp")
    private String Timestamp;
    @JsonProperty("TransactionType")
    private String TransactionType;
    @JsonProperty("Amount")
    private String Amount;
    @JsonProperty("PartyA")
    private String PartyA;
    @JsonProperty("PartyB")
    private String PartyB;
    @JsonProperty("PhoneNumber")
    private String PhoneNumber;
    @JsonProperty("CallBackURL")
    private String CallBackURL;
    @JsonProperty("AccountReference")
    private String AccountReference;
    @JsonProperty("TransactionDesc")
    private String TransactionDesc;
}
