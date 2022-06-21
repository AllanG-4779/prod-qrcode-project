package com.group4.qrcodepayment.exception;

import com.group4.qrcodepayment.dto.UsernameOrEmailExistsDto;
import com.group4.qrcodepayment.exception.resterrors.InvalidUsernameOrPasswordException;
import com.group4.qrcodepayment.exception.resterrors.OtpNotGeneratedException;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(InvalidUsernameOrPasswordException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidUsernameOrPasswordException ex){

         return ResponseEntity.status(401).body(ex.getMessage());
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


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex){
        Map<Object, Object> response = new LinkedHashMap<>();
        response.put("message", ex.getMessage());
        response.put("code", 500);
        response.put("reason", HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                response
        );
    }

}
