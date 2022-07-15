package com.group4.qrcodepayment.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetDto {
    @NotBlank(message = "token is password reset token is required")
    @NotNull(message="pass a request token")
    private String token;
    @NotBlank(message = "password can never be blank")
    @Pattern(regexp = "^(\\d){4}$",
            message = "Only numbers are allowed")
    @NotNull(message= "Did you forget to input your password")
    private String password;
}
