package com.group4.qrcodepayment.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequestDto {
    private String source;
    private Integer amount;
    private String recipientPhone;
}

