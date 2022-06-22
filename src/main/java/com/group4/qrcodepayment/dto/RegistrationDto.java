package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDto {
    @NotBlank(message = "first name cannot be blank")
    @NotNull(message = "first name must be provided")
    private String firstName;
    @NotBlank(message = "this must be provided")
    @NotNull(message="two names are required")

    private String secondName;
//    @NotNull(message= "Username must be provided")
//    @NotBlank(message ="Username is blank, please correct it")
////    private String username;
    @NotBlank(message = "password can never be blank")
    @Pattern(regexp = "^(\\d){4}$",
            message = "Only numbers are allowed")
    @NotNull(message= "Did you forget to input your password")

    private String password;
        @Pattern(regexp = "([17])(\\d){8}", message = "The phone number you entered is not valid")
    private String phone;
    @Pattern(regexp = "([a-zA-Z.\\d])+(@)(\\w){4,10}(.)(\\w){2,5}", message = "Email is not valid ")
    private String email;



}
