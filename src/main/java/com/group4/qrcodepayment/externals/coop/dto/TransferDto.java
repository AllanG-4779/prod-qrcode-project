package com.group4.qrcodepayment.externals.coop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group4.qrcodepayment.util.RandomGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TransferDto {
    @JsonProperty("MessageReference")
    private  String MessageReference ;
    @JsonProperty("CallBackUrl")
    private  String CallBackUrl ;
    @JsonProperty("Source")
    private SourceDto Source;
    @JsonProperty("Destinations")
    private DestinationDto[] Destinations;

}
