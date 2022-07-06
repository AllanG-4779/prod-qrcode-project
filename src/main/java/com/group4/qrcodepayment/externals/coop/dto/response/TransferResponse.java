package com.group4.qrcodepayment.externals.coop.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.externals.coop.dto.DestinationDto;
import com.group4.qrcodepayment.externals.coop.dto.SourceDto;
import lombok.*;

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
@Data
public class TransferResponse{
    @JsonProperty("MessageReference")
    public String messageReference;
    @JsonProperty("MessageDateTime")
    public String messageDateTime;
    @JsonProperty("MessageCode")
    public String messageCode;
    @JsonProperty("MessageDescription")
    public String messageDescription;
    @JsonProperty("Source")
    public Source source;
    @JsonProperty("Destinations")
    public ArrayList<Destination> destinations;
}



