package com.group4.qrcodepayment.dto.Accounts;

import com.group4.qrcodepayment.dto.Accounts.QPayReceiver;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResultDto {
    private String transactionRef;
    private Integer amount;
    private LocalDateTime dateTime;
    private String status;
    private Character transactionType;
    private String sender;
    private QPayReceiver receiver;
}
