package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.config.JWTConfig;
import com.group4.qrcodepayment.dto.*;
import com.group4.qrcodepayment.events.publisher.LoginRegistrationEventPublisher;
import com.group4.qrcodepayment.exception.resterrors.PhoneNotConfirmedException;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.OneTimeCode;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.JWTUtils;
import com.group4.qrcodepayment.security.PasswordConfig;
import com.group4.qrcodepayment.service.OtpServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private LoginRegistrationEventPublisher loginRegistrationEventPublisher;
    @Autowired
    private OtpServiceImpl otpService;
    @Autowired
    private JWTConfig jwtConfig;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping ("/register")
    @ApiOperation(
            value = "Registers first time users",
            nickname = "User registration",
            notes = "Provide all the parameters shown in the request method\n" +
                    "Upon registration, a server error might occur if the OTP request fails" +
                    "due to unverified phone numbers at Twilio",

            response = RegistrationDto.class
    )
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

        userRegistrationService.userRegister(user);
//        publish that registration has been successfully committed
        loginRegistrationEventPublisher.authPublisher(details.getPhone());

         return ResponseEntity.status(201).body(details);


    }

//    THE LOGIN END POINT BEGINS HERE


    @PostMapping("/login")
    @ApiOperation(value="Logs in the user",
            httpMethod = "POST",
            notes = "This endpoint takes login credentials i.e. username or phone and a password " +
                    " and returns a token that used by the client to submit any request that accesses a protected.\n" +
                    "route",
            response = JWTokenDto.class,
            nickname = "User login",
            produces = "A token"
    )

    public JWTokenDto login(@RequestBody @Valid LoginDto loginCredentials)
            throws Exception {
//        check if the user account is confirmed

//        login the user
        logger.info("Authentication process has commenced for user "+ loginCredentials.getPhoneOrEmail());

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginCredentials.getPhoneOrEmail(),
                            loginCredentials.getPassword()
                    )
            );
        }catch(BadCredentialsException e){
          throw new Exception("Login failed, please try again",e );
       }

//        Having passed the test: Authentication test, now create the token
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginCredentials.getPhoneOrEmail());
//        check if account is enabled
        if(!userDetails.isEnabled()){

            throw new PhoneNotConfirmedException("Account not activated please activate by visiting /auth/verify/");

        }
        String token = jwtUtils.generateToken(userDetails);
logger.info("Login token is "+ token);
       return  JWTokenDto.builder()
                               .token(token)
               .iat(LocalDateTime.now())
               .exp(LocalDateTime.now().plusSeconds(jwtConfig.getExpire()))
                .build();
    }

//    Verify otp
    @PostMapping("/otp/validate")
    @ApiOperation(
            value="Performs OTP verification",
            notes = " Pass a number to which a code has been sent and the code itself\n" +
                    "You will receive an OK response or a Forbidden response depending on the " +
                    "correctness of the OTP"

    )
    public ResponseEntity<Object> validateOTP(@RequestBody VerificationCode code){

        OneTimeCode otpDto = otpService.getOtp(code.getPhone());
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("Returned OTP " + otpDto.toString());
        Map<Object, Object> map = new LinkedHashMap<>();
        String message = null;
//        Check if the code submitted is the same and has not expired

        if(otpDto.getCode().equals(code.getCode()) &&
                LocalDateTime.now().isBefore(otpDto.getExpireAt())
        ){


//            SET THE ACCOUNT AS VERIFIED
            userRegistrationService.setAccountVerified(code.getPhone());

            map.put("code", 201);
            map.put("message", "Successfully verified");
            map.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(200).body(map);

        }

        map.put("code", 403);
        map.put("message", HttpStatus.FORBIDDEN);
        map.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(403).body(map);


    }
    @GetMapping("/registration/verify/{phone}")
    @ApiOperation(
               value="Checks whether a user is registered",
               httpMethod = "POST",
                notes = "Accepts a phone number is a desired format and returns a OK response if user is found" +
                        " or A 404 if no user matches the passed details"
                            )
    public ResponseEntity<Object> verifyRegistration(@PathVariable String phone) throws SQLException {

         Boolean user = userRegistrationService.numberRegistered(phone);
         Map<Object, Object> res = new LinkedHashMap<>();
//         User with the phone number is found
         if (user){
             res.put("code", 200);
             res.put("message", "Number registered");
             res.put("timestamp", LocalDateTime.now());
             return ResponseEntity.status(200).body(res);
         }
//         User with the phone number is not registered
             res.put("code", 404);
             res.put("message", "Number not found");
             res.put("timestamp", LocalDateTime.now());
             return ResponseEntity.status(404).body(res);

    }
    @PostMapping("/otp/send")
    @ApiOperation(
            value="Sends OTP",
            notes = "Send an OTP to a specified number"

                )
    public void sendOtp(@RequestBody RegistrationVerification phone){

        loginRegistrationEventPublisher.authPublisher(phone.getPhone());

    }

}
