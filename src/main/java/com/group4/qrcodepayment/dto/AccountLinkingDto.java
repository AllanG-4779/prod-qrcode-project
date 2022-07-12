package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLinkingDto {
    @NotNull(message = "Bank ID is required")
    private String bankId;
    @NotNull(message = "Account number cannot be null")
    private String accountNumber;
}
