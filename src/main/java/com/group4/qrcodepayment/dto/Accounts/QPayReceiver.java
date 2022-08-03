package com.group4.qrcodepayment.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QPayReceiver {
//    How much did you receive
    private String account;
    private String fullName;
    private Integer amount;
    private String transactionRef;
    private Character transactionType;


}
