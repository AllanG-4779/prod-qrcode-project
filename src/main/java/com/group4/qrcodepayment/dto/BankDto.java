package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankDto {
    private String ipslCode;
    private String name;
    private boolean supported;
}
