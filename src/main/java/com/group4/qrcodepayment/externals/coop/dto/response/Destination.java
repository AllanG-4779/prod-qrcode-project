package com.group4.qrcodepayment.externals.coop.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Destination {
    @JsonProperty("TransactionID")
    public String transactionID;
    @JsonProperty("ResponseCode")
    public String responseCode;
    @JsonProperty("ResponseDescription")
    public String responseDescription;
    @JsonProperty("ReferenceNumber")
    public String ReferenceNumber;
    @JsonProperty("AccountNumber")
    public String AccountNumber;
    @JsonProperty("BankCode")
    public String BankCode;
    @JsonProperty("Amount")
    public String Amount;
    @JsonProperty("TransactionCurrency")
    public String TransactionCurrency;
    @JsonProperty("Narration")
    public String Narration;


}
