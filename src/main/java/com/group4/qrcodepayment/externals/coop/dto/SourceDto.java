package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SourceDto {
    @JsonProperty("AccountNumber")
    public String AccountNumber;
    @JsonProperty("Amount")
    public String Amount;
    @JsonProperty("TransactionCurrency")
    public  String TransactionCurrency;
    @JsonProperty("Narration")
    public String Narration;

}
