package com.group4.qrcodepayment.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QP2QPTransactionResultDto {
    private Summary sender;
    private Summary receiver;
    private String amount;
    private String transactionTime;
    private String transactionRef;
}
