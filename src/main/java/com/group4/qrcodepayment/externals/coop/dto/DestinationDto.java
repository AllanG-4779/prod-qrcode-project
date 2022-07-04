package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.util.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "parentTransfer")
@AllArgsConstructor
@NoArgsConstructor
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
