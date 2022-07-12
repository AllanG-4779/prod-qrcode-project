package com.group4.qrcodepayment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLinkingDto {
    @NotNull(message = "Bank ID is required")
    @Length(min=1, max=3, message = "Length between 9 and 15")
    @ApiModelProperty(
            allowableValues = "integers",
            name = "IPSL bank code"
    )
    private String bankId;
    @Length(min=9, max=15)
    @Pattern(regexp = "\\d+", message = "Account number cannot contain characters")
    @NotNull(message = "Account number cannot be null")
    @ApiModelProperty(
            name = "Account number of the bank to be linked"
    )
    private String accountNumber;
}
