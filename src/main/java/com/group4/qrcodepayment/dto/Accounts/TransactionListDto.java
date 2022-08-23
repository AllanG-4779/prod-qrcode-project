package com.group4.qrcodepayment.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionListDto {
    private String transactionRef;
    private String amount;
    private String dateTime;
    private String sourceAccount;
    private String destinationAccount;
    private String status;
    private Character transactionType;

}
