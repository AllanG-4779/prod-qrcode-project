package com.group4.qrcodepayment.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Summary{
    private String beforeBal;
    private String afterBal;


}