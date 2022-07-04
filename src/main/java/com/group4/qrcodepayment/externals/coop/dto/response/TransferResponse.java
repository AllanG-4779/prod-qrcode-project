package com.group4.qrcodepayment.externals.coop.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferResponse{
    @JsonProperty("MessageReference")
    public String messageReference;
    @JsonProperty("MessageDateTime")
    public LocalDateTime messageDateTime;
    @JsonProperty("MessageCode")
    public String messageCode;
    @JsonProperty("MessageDescription")
    public String messageDescription;
    @JsonProperty("Source")
    public Source source;
    @JsonProperty("Destinations")
    public ArrayList<Destination> destinations;
}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

class Destination extends DestinationDto {
    @JsonProperty("TransactionID")
    public String transactionID;
    @JsonProperty("ResponseCode")
    public String responseCode;
    @JsonProperty("ResponseDescription")
    public String responseDescription;

    @Builder(builderMethodName = "childsourcebuilder")
    public Destination ( String ReferenceNumber, String AccountNumber,
                         String BankCode, String Amount, String TransactionCurrency,
                         String Narration, String transactionID, String responseCode, String responseDescription){
        super(ReferenceNumber, AccountNumber, BankCode, Amount, TransactionCurrency, Narration);
        this.transactionID = transactionID;
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;

    }



}


@AllArgsConstructor
@NoArgsConstructor

class Source extends SourceDto {


    @JsonProperty("ResponseCode")
    public String responseCode;
    @JsonProperty("ResponseDescription")
    public String responseDescription;

    @Builder(builderMethodName = "childBuilder")
    public Source(String AccountNumber, String Amount, String Narration, String TransactionCurrency
    , String responseDescription, String responseCode){
        super(AccountNumber, Amount, Narration, TransactionCurrency);
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;

    }
}



