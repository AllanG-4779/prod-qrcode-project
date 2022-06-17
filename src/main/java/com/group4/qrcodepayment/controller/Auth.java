package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.JWTokenDto;
import com.group4.qrcodepayment.dto.LoginDto;
import com.group4.qrcodepayment.dto.RegistrationDto;
import com.group4.qrcodepayment.exception.resterrors.InvalidUsernameOrPasswordException;
import com.group4.qrcodepayment.exception.resterrors.UsernameOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.JWTUtils;
import com.group4.qrcodepayment.security.PasswordConfig;
import com.group4.qrcodepayment.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
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
                .firstName(details.getFirstName())
                .secondName(details.getSecondName())
                .build();

        userRegistrationService.userRegister(user);

         return ResponseEntity.status(201).body(details);

    }
    @PostMapping("/login")
    public JWTokenDto login(@RequestBody LoginDto loginCredentials)
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
}
