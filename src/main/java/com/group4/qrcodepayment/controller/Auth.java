package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.config.JWTConfig;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.Auth.*;
import com.group4.qrcodepayment.events.handler.LoginRegisterEventHandler;
import com.group4.qrcodepayment.events.publisher.LoginRegistrationEventPublisher;
import com.group4.qrcodepayment.exception.resterrors.PhoneNotConfirmedException;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.exception.resterrors.RegistrationFailedException;
import com.group4.qrcodepayment.exception.resterrors.TwilioFailedException;
import com.group4.qrcodepayment.models.OneTimeCode;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.security.CustomUserDetails;
import com.group4.qrcodepayment.security.JWTUtils;
import com.group4.qrcodepayment.security.PasswordConfig;
import com.group4.qrcodepayment.service.OtpServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationImpl;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.swagger.annotations.ApiOperation;
import org.jasypt.exceptions.PasswordAlreadyCleanedException;
import org.jasypt.util.text.StrongTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
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
    private UserRegistrationImpl userRegistrationService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private UserRepoInt userRepo;
    @Autowired
    private LoginRegistrationEventPublisher loginRegistrationEventPublisher;
    @Autowired
    private OtpServiceImpl otpService;
    @Autowired
    private JWTConfig jwtConfig;
    @Autowired
    private LoginRegisterEventHandler registerEventHandler;
    @Autowired
    private TwilioConfig twilioConfig;

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
    public ResponseEntity<Object> register(@RequestBody @Valid RegistrationDto details )
            throws PhoneOrEmailExistsException, RegistrationFailedException{

//        check if user is available
       userRegistrationService.checkUserNameExists(details.getPhone());
       userRegistrationService.checkEmailExists(details.getEmail());

        UserInfo user = UserInfo.builder()
                .email(details.getEmail())
                .isConfirmed(false)
                .password(passwordConfig.passwordEncoder().encode(details.getPassword()))
                .phone(details.getPhone())
                .roles("USER")
                .idNo(details.getIdNo())

                .firstName(details.getFirstName())
                .secondName(details.getSecondName())
                .build();



//        Sea message for successful registration
        try{
            userRegistrationService.userRegister(user);
            Message.creator(
                    new PhoneNumber("+254"+user.getPhone()),
                    new PhoneNumber(twilioConfig.getTrialNumber()),
                    "Thank you "+ user.getFirstName()+" "+user.getSecondName() +
                            " for registering on QPay Platform\n" +
                            "You won't regret getting on board\n" +
                            "Enjoy!!!"
            ).create();


        }catch(ApiException ex){
            throw new TwilioFailedException(ex.getMessage(), "Cannot send SMS at this time");
        }


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
        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
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
    public ResponseEntity<Object> validateOTP(@RequestBody @Valid VerificationCode code){

        OneTimeCode otpDto = otpService.getOtp(code.getPhone());
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("Returned OTP " + otpDto.toString());
        Map<Object, Object> map = new LinkedHashMap<>();
        String accountVerifiedToken = null;
//        Check if the code submitted is the same and has not expired

        if(otpDto.getCode().equals(code.getCode()) &&
                LocalDateTime.now().isBefore(otpDto.getExpireAt())
        ){
            UserInfo account = userRegistrationService.findUserByPhone
                    (code.getPhone());
            try{
                if(account.getAccountFlagged()){
                    StrongTextEncryptor passResetToken = new StrongTextEncryptor();
                    passResetToken.setPassword("pass");
//                Encrypt the user details and the time the code was stamped
                    String message = LocalDateTime.now()+"="+userRegistrationService
                            .findUserByPhone(code.getPhone()).getPhone();
                    String encrypted = passResetToken.encrypt(message);
                    otpService.updateOtp(encrypted,code.getPhone());
                    map.put("securityToken",encrypted);
                }

            }catch (NullPointerException ex){
                map.put("description","User unknown to the system");
            }

            map.put("code", 201);
            map.put("message", "Successfully verified");
            map.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(200).body(map);

        }

        map.put("code",HttpStatus.FORBIDDEN );
        map.put("message", "Invalid OTP please ask for another one");
        map.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(403).body(map);


    }
    @GetMapping("/registration/verify")
    @ApiOperation(
               value="Checks whether a user is registered",
               httpMethod = "GET",
                notes = "Accepts a phone number is a desired format and returns a OK response if user is found" +
                        " or A 404 if no user matches the passed details"
                            )
    public ResponseEntity<Object> verifyRegistration(@RequestParam String phone) throws SQLException {

         Boolean user = userRegistrationService.numberRegistered(phone);
         UserInfo userFound = userRegistrationService.findUserByPhone(phone);
         Map<Object, Object> res = new LinkedHashMap<>();
//         User with the phone number is found
         if (user){
             Map<Object, Object> user1 = new LinkedHashMap<>();
             user1.put("email", userFound.getEmail());
             user1.put("fullName",userFound.getFirstName()+" "+userFound.getSecondName() );
             res.put("code", 200);
             res.put("message", "Number registered");

             res.put("user",user1 );
             res.put("timestamp", LocalDateTime.now());
             return ResponseEntity.status(200).body(res);
         }
//         User with the phone number is not registered
             res.put("code", 404);
             res.put("message", "Number not found");
             res.put("timestamp", LocalDateTime.now());
             return ResponseEntity.status(404).body(res);

    }
    @GetMapping("/otp/send")
    @ApiOperation(
            value="Sends OTP",
            notes = "Send an OTP to a specified number"

                )
    public void sendOtp (@RequestParam @Valid RegistrationVerification phone){

        loginRegistrationEventPublisher.authPublisher(phone.getPhone());

    }
    @GetMapping("/forgot")
    public void forgotPassword(@RequestParam String phoneNumber){
         userRegistrationService.flagAccount(phoneNumber, true);
//        Given the phone number, send the otp

       String code = registerEventHandler.getOTP();
        Message.creator(
                new PhoneNumber("+254"+phoneNumber),
                new PhoneNumber(twilioConfig.getTrialNumber()),

                "\nIf you've requested an OTP to reset your password use the code below\n" +
                        "The code expires in 1 minute\n\n\n"+code
        ).create();
//Save the otp
        OtpDto otpDto = OtpDto.builder().code(code)
                .expireAt(LocalDateTime.now()
                        .plusMinutes(1))
                .issueAt(LocalDateTime.now())
                .owner(phoneNumber)
                .build();

        otpService.addOtp(otpDto);
    }
    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto)
            throws RegistrationFailedException {
//        User wants to change the password, he/she hasn't forgotten it
        LinkedHashMap<String, String> res = new LinkedHashMap<>();
        res.put("message", "password changed successfully");
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         if(auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken) ){
             String encodedPassword = passwordConfig.passwordEncoder().encode(passwordResetDto.getPassword());
             userRegistrationService.updateUserpassword(auth.getName(),encodedPassword);
             return ResponseEntity.status(201).body(res);
         }

//         Verify the token passed
        OneTimeCode otpToken = otpService.getOtpByCode(passwordResetDto.getToken());
        if (otpToken == null){
            throw new RuntimeException("Password reset request aborted");
        }
        StrongTextEncryptor encryptor = new StrongTextEncryptor();
        encryptor.setPassword("pass");

        String [] details = encryptor.decrypt(otpToken.getCode()).split("=");
        if(!LocalDateTime.now().isBefore(LocalDateTime.parse(details[0]).plusMinutes(30))){

            throw new RuntimeException("Password reset token has expired");
        }
//        otherwise find the user
        UserInfo user = userRegistrationService.findUserByPhone(details[1]);

//        update the user
       String newPassword = passwordConfig.passwordEncoder()
               .encode(passwordResetDto.getPassword());

        userRegistrationService.updateUserpassword(user.getPhone(),newPassword);

//        delete the token from the db
        otpService.deleteOtp(details[1]);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("message", "Password created successfully");
        userRegistrationService.flagAccount(user.getPhone(),false);

        return ResponseEntity.status(201).body(map);

    }

}
