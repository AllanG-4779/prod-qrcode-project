package com.group4.qrcodepayment.externals.mpesa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MpesaFundAccount {
    private String BusinessShortCode;
    private String Password;
    private Timestamp Timestamp;
    private String TransactionType;
    private Integer Amount;
    private Integer PartyA;
    private Integer partyB;
    private Integer PhoneNumber;
    private String CallBackURL;
    private String AccountReference;
    private String TransactionDesc;
}
