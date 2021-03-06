package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.util.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDto {
    @JsonProperty("ReferenceNumber")
    public  String ReferenceNumber;
    @JsonProperty("AccountNumber")
    public String AccountNumber;
    @JsonProperty("BankCode")
    public   String BankCode;
    @JsonProperty("Amount")
    public String Amount;
    @JsonProperty("TransactionCurrency")
    public String TransactionCurrency;
    @JsonProperty("Narration")
    public String Narration;
}
