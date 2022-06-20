package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.dto.*;
import com.group4.qrcodepayment.events.publisher.LoginRegistrationEventPublisher;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.OneTimeCode;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.JWTUtils;
import com.group4.qrcodepayment.security.PasswordConfig;
import com.group4.qrcodepayment.service.OtpServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class Auth {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordConfig passwordConfig;
    @Autowired
    private UserRegistrationService userRegistrationService;
    @Autowired
    AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;
    @Autowired
    private LoginRegistrationEventPublisher loginRegistrationEventPublisher;
    @Autowired
    private OtpServiceImpl otpService;
    @PostMapping ("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto details ) throws PhoneOrEmailExistsException {

//        check if user is available
       userRegistrationService.checkUserNameExists(details.getPhone());
       userRegistrationService.checkEmailExists(details.getEmail());

        UserInfo user = UserInfo.builder()
                .email(details.getEmail())
                .isConfirmed(false)
                .password(passwordConfig.passwordEncoder().encode(details.getPassword()))
                .phone(details.getPhone())
                .roles("USER")

                .firstName(details.getFirstName())
                .secondName(details.getSecondName())
                .build();

        //userRegistrationService.userRegister(user);
//        publish that registration has been successfully committed
        loginRegistrationEventPublisher.authPublisher(details);

//         return ResponseEntity.status(201).body(details);
        return null;

    }
    @PostMapping("/login")
    public JWTokenDto login(@RequestBody @Valid LoginDto loginCredentials)
            throws Exception {
//        login the user
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.getUsername(),
                            loginCredentials.getPassword()
                    )
            );
        }catch(BadCredentialsException e){
          throw new Exception("Login failed, please try again with other credentials",e );
       }
//        Having passed the test: Authentication test, now create the token
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginCredentials.getUsername());
        String token = new JWTUtils().generateToken(userDetails);

       return  JWTokenDto.builder()
                .token(token)
                .user_id(userDetails.getUsername())
                .build();
    }

//    Verify otp
    @PostMapping("/verify")
    public String verify(@RequestBody VerificationCode code){

        OneTimeCode otpDto = otpService.getOtp(code.getPhone());
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug(otpDto.toString());
        if(otpDto.getCode().equals(code.getCode())){
            return "Phone number successfully verified";
        }

        return "Invalid OTP please try again after a minute";


    }
}
