package com.group4.qrcodepayment.externals.mpesa.dto;

import lombok.Data;

@Data
public class TransactionMetadata {
    private Integer amount;
    private String receipt;
    private Long phoneNumber;
}
