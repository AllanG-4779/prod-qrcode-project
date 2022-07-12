package com.group4.qrcodepayment.controller;

import com.group4.qrcodepayment.Repositories.AccountRepo;
import com.group4.qrcodepayment.Repositories.BankRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.AccountLinkingDto;
import com.group4.qrcodepayment.exception.resterrors.AccountLinkFailedException;
import com.group4.qrcodepayment.exception.resterrors.UnsupportedBankException;
import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/acc")
public class Accounts {
    @Autowired
    private BankRepo repo;
    @Autowired
    private UserRepoInt userRepoInt;
    @Autowired
    private AccountServiceImpl accountService;
    @PostMapping("/link")
    public AccountLinkingDto linkAccount(@RequestBody @Valid AccountLinkingDto accountLink) throws UnsupportedBankException, AccountLinkFailedException {

        return accountService.linkAccount(accountLink);

    }

}
