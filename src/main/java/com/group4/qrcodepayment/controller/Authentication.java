package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.RegistrationDto;
import com.group4.qrcodepayment.exception.resterrors.UsernameOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.PasswordConfig;
import com.group4.qrcodepayment.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class Authentication {
    @Autowired
    private UserRepoInt userRepo;
    @Autowired
    private PasswordConfig passwordConfig;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @PostMapping ("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto details ) throws UsernameOrEmailExistsException{

//        check if user is available
       userRegistrationService.checkUserNameExists(details.getUsername());
       userRegistrationService.checkEmailExists(details.getEmail());

        UserInfo user = UserInfo.builder()
                .email(details.getEmail())
                .isConfirmed(false)
                .password(passwordConfig.passwordEncoder().encode(details.getPassword()))
                .phone(details.getPhone())
                .roles("USER")
                .username(details.getUsername())
                .build();

        userRegistrationService.userRegister(user);

         return ResponseEntity.status(201).body(details);

    }
}
