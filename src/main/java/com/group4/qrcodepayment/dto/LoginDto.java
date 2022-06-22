package com.group4.qrcodepayment.dto;

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
    private String phoneOrEmail;
    @NotNull
    @NotBlank
    private String password;
}
