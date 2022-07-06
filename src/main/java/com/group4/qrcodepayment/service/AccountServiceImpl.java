package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.AccountRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private UserRepoInt userRepoInt;
    @Autowired
    private BankServiceImpl bankService;
    @Autowired
    private AccountRepo accountRepo;

    @Override
    public String getUserAccountNumber() {
        String username = null;
//        Get the currently logged-in user
        Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken)){
            throw new RuntimeException("You are not authenticated");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails){
             username = ((UserDetails) principal).getUsername();
        }


        UserInfo loggedUser = userRepoInt.findByUsername(username);


//        Get the bank of currently logged in user: which is coperative bank
        Bank copBank = bankService.getBankById("11");

        return accountRepo
                .findAccountByUserIdAndBankId(loggedUser,copBank).getAccountNumber();
    }
}
