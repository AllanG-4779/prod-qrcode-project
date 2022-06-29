package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.util.RandomGenerator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DestinationDto {
    @JsonProperty("ReferenceNumber")
    private  String ReferenceNumber;
    @JsonProperty("AccountNumber")
    private String AccountNumber;
    @JsonProperty("BankCode")
    private  String BankCode;
    @JsonProperty("Amount")
    private String Amount;
    @JsonProperty("TransactionCurrency")
    private String TransactionCurrency;
    @JsonProperty("Narration")
    private String Narration;
}
