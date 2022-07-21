package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankDto {
   @NotNull
   @NotBlank(message = "Bank code is required")
    private String ipslCode;
   @NotBlank(message = "Bank name must be there")
    private String name;
    private boolean supported=false;
}
