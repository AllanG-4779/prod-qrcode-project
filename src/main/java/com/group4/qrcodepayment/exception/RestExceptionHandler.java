package com.group4.qrcodepayment.exception;

import com.group4.qrcodepayment.customresponse.RegistrationResponse;
import com.group4.qrcodepayment.dto.UsernameOrEmailExistsDto;
import com.group4.qrcodepayment.exception.resterrors.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends Exception {

     @ExceptionHandler(MethodArgumentNotValidException.class)
     @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<Object, Object> handleInvalidRequest(MethodArgumentNotValidException ex){

         Map<Object, Object> map = new LinkedHashMap<>();
         List<FieldError> list;
              list   = ex.getBindingResult().getFieldErrors();
          list.forEach(err->map.put(err.getField(), err.getDefaultMessage()));
          return map;

     }

    @ExceptionHandler(PhoneOrEmailExistsException.class)
    public ResponseEntity<UsernameOrEmailExistsDto> handleUsernameOrEmailException(PhoneOrEmailExistsException ex){


        UsernameOrEmailExistsDto user =  UsernameOrEmailExistsDto.builder()
                .code(601)
                .message(ex.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(user,HttpStatus.CONFLICT);


    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleInvalidCredentials(UsernameNotFoundException ex){

         return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler(OtpNotGeneratedException.class)
     public ResponseEntity<Object> handleNoOtpGeneratedException(OtpNotGeneratedException ex){
         Map<Object, Object> response = new LinkedHashMap<>();
         response.put("message", "OTP not generated for this number");
         response.put("code", 406);
         response.put("reason", HttpStatus.NOT_ACCEPTABLE);
         return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                 response
         );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
        Map<Object, Object> response = new LinkedHashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", 500);
        response.put("reason", "Something went wrong on our end");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                response
        );
    }
    @ExceptionHandler(TwilioFailedException.class)
    public ResponseEntity<?> handleTwilioError(TwilioFailedException ex){
         Map<Object, Object> response = new LinkedHashMap<>();
         response.put("code", 408);
         response.put("message", ex.getUserMessage());
         response.put("sentAt", LocalDateTime.now());
         response.put("debugMessage", ex.getMessage());

        return ResponseEntity.status(408).body(response);
    }
@ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<?> handleRegistrationException(RegistrationFailedException ex){
         return ResponseEntity.status(500).body(RegistrationResponse.builder()
                         .reason(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                         .code(500)
                         .message(ex.getMessage())


                 .build());
}
//Hdndle user account disabled exception
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex){
         LinkedHashMap<String, Object> map = new LinkedHashMap<>();
         map.put("code", 403);
         map.put("message", "Your account is locked, please verify your phone number to unlock it");
         map.put("debugMessage", ex.getMessage());
         return ResponseEntity.status(403).body(map);
    }
//    handle the account number is null
    @ExceptionHandler(BankLinkedException.class)
    public ResponseEntity<?> handleBankNotLinkedException(BankLinkedException ex){
         Map<String, Object> map = new LinkedHashMap<>();
         map.put("code", 404);
         map.put("message", ex.getMessage());
         map.put("debugMessage", ex.getDebugMessage());
         return ResponseEntity.status(404).body(map);
    }
    @ExceptionHandler(UnsupportedBankException.class)
public ResponseEntity<?> handleUnsupportedBankException(UnsupportedBankException ex){
          Map<String, Object> object = new LinkedHashMap<>();
          object.put("code", 499);
          object.put("message", ex.getMessage());
          object.put("reason", "Bank not supported");
          return ResponseEntity.status(499).body(object);

}
@ExceptionHandler(AccountLinkFailedException.class)
    public ResponseEntity<?> handleAccountLinkException(AccountLinkFailedException ex){
        Map<String, Object> object = new LinkedHashMap<>();
        object.put("code", 409);
        if(ex.getMessage().contains("constraint [accountOwner]")){
            object.put("message", "Account is already linked");
            object.put("debugMessage","Duplicate entry for the same bank and owner");
        }else{

            object.put("message", "Something went wrong when linking your account");
            object.put("debugMessage", ex.getMessage());
        }


        return ResponseEntity.status(409).body(object);

    }
    @ExceptionHandler(CopBankTransactionException.class)
    public ResponseEntity<?> handleCopBankTransactionException(CopBankTransactionException e){
        Map<Object, Object> map  = new LinkedHashMap<>();
        map.put("code", HttpStatus.BAD_REQUEST);
        map.put("message", "Request cannot be completed at the moment");
        map.put("debugMessage", e.getMessage());
         return ResponseEntity.status(400).body(
                 map
         );

    }
    @ExceptionHandler({AuthenticationNotFoundException.class, ExpiredJwtException.class})
    public ResponseEntity<?> handleAuthentication(AuthenticationNotFoundException ex){
         ExceptionRes res  = ExceptionRes.builder()
                 .code(401)
                 .message("The system cannot verify your identity")
                 .debugMessage(ex.getMessage())
                 .build();
         return ResponseEntity.status(401).body(res);
    }
    @ExceptionHandler(InvalidJWTException.class)
    public ResponseEntity<?>handleExpiredJwtException(InvalidJWTException ex){
         ExceptionRes res = ExceptionRes.builder()
                 .debugMessage(ex.getMessage())
                 .code(403)
                 .message("Authentication or Authorization failed")
                 .build();
         return ResponseEntity.status(403).body(res);
    }
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<?> handleTransactionNotSpecifiedException(TransactionNotFoundException ex){

         return  ResponseEntity.status(400).body(ExceptionRes.builder().code(400)
                 .message("Transaction type not specified")
                 .debugMessage(ex.getMessage()).build());

    }
    @ExceptionHandler(BankNotFoundException.class)
    public ResponseEntity<?> handleBankNotFoundException(BankNotFoundException e){
         return ResponseEntity.status(404).body(
                 ExceptionRes.builder()
                         .message("Resource not found")
                         .debugMessage(e.getMessage())
                         .code(404).build()
         );
    }
@ExceptionHandler(TransactionFailedException.class)
   public ResponseEntity<?> handleTransactionFailedException(TransactionFailedException e){
         ExceptionRes res = ExceptionRes.builder()
                 .code(500)
                 .message(e.getMessage())
                 .debugMessage(e.getLocalizedMessage())
                  .build();
         return ResponseEntity.status(500).body(res);
   }
}
