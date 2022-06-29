package com.group4.qrcodepayment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull(message = "Login parameter cannot be null")
    @NotBlank(message = "You must provide a user name")
    @ApiModelProperty(name = "Clients phone number or email address",
            required = true,
             allowableValues = "String")
    private String phoneOrEmail;
    @NotNull
    @NotBlank
    @ApiModelProperty(notes = "A four digit pin secret",
            name="PIN", allowableValues = "Numbers")
    private String password;
}
