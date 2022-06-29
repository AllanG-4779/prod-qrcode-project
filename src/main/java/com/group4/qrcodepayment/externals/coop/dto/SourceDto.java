package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SourceDto {
    @JsonProperty("AccountNumber")
    private String AccountNumber;
    @JsonProperty("Amount")
    private String Amount;
    @JsonProperty("TransactionCurrency")
    private String TransactionCurrency;
    @JsonProperty("Narration")
    private String Narration;

}
