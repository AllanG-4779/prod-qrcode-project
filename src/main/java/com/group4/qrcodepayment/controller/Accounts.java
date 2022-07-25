package com.group4.qrcodepayment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.zxing.WriterException;
import com.group4.qrcodepayment.Repositories.BankRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.AccountLinkingDto;
import com.group4.qrcodepayment.dto.BankDto;
import com.group4.qrcodepayment.exception.resterrors.AccountLinkFailedException;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.UnsupportedBankException;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.service.BankServiceImpl;
import com.group4.qrcodepayment.util.QRCodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.util.ByteArrayBuffer;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/acc")
@CrossOrigin(origins = "*")
public class Accounts {

    private BankRepo repo;

    private UserRepoInt userRepoInt;
    private BankServiceImpl bankService;
    private AccountServiceImpl accountService;

    private QRCodeGenerator qrCodeGenerator;
    @PostMapping("/link")
    public AccountLinkingDto linkAccount(@RequestBody @Valid AccountLinkingDto accountLink)
            throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException {


        return accountService.linkAccount(accountLink);

    }


    @GetMapping("/myqr-code")
    public byte[] generateMyQr(@RequestParam int width, @RequestParam int  height) throws IOException, WriterException, AuthenticationNotFoundException, JSONException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if(!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
        throw new AuthenticationNotFoundException("Seems you are not authenticated, please login ");
    }
    UserInfo user = userRepoInt.findByUsername(authentication.getName());
    if (user==null){
        throw new UserPrincipalNotFoundException("user not found");
    }
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        String key = "Cnd80751xh.10@27.com";
//        set the key
        textEncryptor.setPassword(key);
//        encrypt the values
        String message = "user_id="+user.getUserId()+"phone="+user.getPhone();
        String hash = textEncryptor.encrypt(message);


          return     QRCodeGenerator.getQRcodeImage(hash,width,height);



    }
    @Bean
    public ByteArrayHttpMessageConverter httpMessageConverter(){
        return new ByteArrayHttpMessageConverter();
    }


}
