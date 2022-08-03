package com.group4.qrcodepayment.dto.Accounts;

import com.group4.qrcodepayment.dto.Accounts.TransactionListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDto {
    private String userId;
    private Integer qpayBalance;
    private List<TransactionListDto> transactionLists;
}
