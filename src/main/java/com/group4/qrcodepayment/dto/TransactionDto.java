package com.group4.qrcodepayment.dto;

import com.group4.qrcodepayment.models.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private String transactionRef;
    private String transactionAmount;
    private String sourceAccount;
    private String destinationAccount;
    private LocalDateTime date;
    private Character transactionType;
    private String status;
    private UserInfo userId;
}
