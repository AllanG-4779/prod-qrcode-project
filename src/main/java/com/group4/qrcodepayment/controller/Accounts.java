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
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.service.BankServiceImpl;
import com.group4.qrcodepayment.util.QRCodeGenerator;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/acc")
public class Accounts {
    @Autowired
    private BankRepo repo;
    @Autowired
    private UserRepoInt userRepoInt;
    @Autowired
    private BankServiceImpl bankService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;
    @PostMapping("/link")
    public AccountLinkingDto linkAccount(@RequestBody @Valid AccountLinkingDto accountLink) throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException {

        return accountService.linkAccount(accountLink);

    }
    @PostMapping("/add")
    public Bank addBank(@RequestBody @Valid BankDto bank){

        return bankService.addNewBank(bank);
    }

    @GetMapping("/myqr-code")
    public byte[] generateMyQr(@RequestParam int width, @RequestParam int  height) throws IOException, WriterException, AuthenticationNotFoundException {
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

       return QRCodeGenerator.getQRcodeImage(hash, width, height);

    }

}
