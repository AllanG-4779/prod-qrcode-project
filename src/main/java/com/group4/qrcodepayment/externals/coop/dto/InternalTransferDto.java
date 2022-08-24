package com.group4.qrcodepayment.externals.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalTransferDto {
    private String receiver;
    private String amount;
}
