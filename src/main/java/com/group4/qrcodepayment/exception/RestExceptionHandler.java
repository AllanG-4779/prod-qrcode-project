package com.group4.qrcodepayment.exception;

import com.group4.qrcodepayment.dto.UsernameOrEmailExistsDto;
import com.group4.qrcodepayment.exception.resterrors.UsernameOrEmailExistsException;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @ExceptionHandler(UsernameOrEmailExistsException.class)
    public ResponseEntity<UsernameOrEmailExistsDto> handleUsernameOrEmailException(UsernameOrEmailExistsException ex){


        UsernameOrEmailExistsDto user =  UsernameOrEmailExistsDto.builder()
                .code(601)
                .message(ex.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(user,HttpStatus.CONFLICT);


    }


}
