package com.group4.qrcodepayment.externals.coop.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class Source {
    @JsonProperty("ResponseCode")
    public String responseCode;
    @JsonProperty("ResponseDescription")
    public String responseDescription;
    @JsonProperty("AccountNumber")
    public String AccountNumber;
    @JsonProperty("Amount")
    public String Amount;
    @JsonProperty("Narration")
    public String Narration;
    @JsonProperty("TransactionCurrency")
    public String TransactionCurrency;


}
