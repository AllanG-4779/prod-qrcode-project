package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {
@NotNull(message= "Username must be provided")
@NotBlank(message ="Username is blank, please correct it")
private String username;
@NotBlank(message = "password can never be blank")
@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~`!@$%#^&*()-=+\"?'/<>]).{10,20}$",
        message = "Password security checks failed")

private String password;
    @Pattern(regexp = "([17])(\\d){8}", message = "The phone number you entered is not valid")
private String phone;
@Pattern(regexp = "(\\w)+(@)(\\w){4,10}(.)(\\w){2,5}", message = "Email is not valid ")
private String email;
}
