package com.group4.qrcodepayment.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationVerification {
  @Pattern(regexp = "([17])(\\d){8}", message = "The phone number you entered is not valid")
  private String phone;
}
