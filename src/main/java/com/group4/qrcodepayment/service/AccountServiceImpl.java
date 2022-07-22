package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.AccountRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.AccountLinkingDto;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.AccountLinkFailedException;
import com.group4.qrcodepayment.exception.resterrors.BankLinkedException;
import com.group4.qrcodepayment.exception.resterrors.UnsupportedBankException;
import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService{
    @Autowired
    private UserRepoInt userRepoInt;

    @Autowired
    private BankServiceImpl bankService;
   @Autowired
   private AccountRepo accountRepo;

    @Autowired
    private UserRegistrationImpl userRegistration;

    private Logger logger;

    @Override
    public String getUserAccountNumber() throws BankLinkedException {
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
        Bank copBank = bankService.getBankByIpslCode("11");
try{
    return accountRepo
            .findAccountByUserIdAndBankId(loggedUser,copBank).getAccountNumber();

}catch(NullPointerException ex){
    throw new BankLinkedException("Source Bank account number not found", ex.getMessage());
}

    }
public AccountLinkingDto linkAccount(AccountLinkingDto accountLink) throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException {
        logger = LoggerFactory.getLogger(this.getClass());
        //verify if the bank is supported
        Bank bank =  bankService.getBankByIpslCode(accountLink.getBankId());
        if (bank == null || !bank.getSupported()){
            throw new UnsupportedBankException("Bank not supported yet.");
        }

//     get the currently logged-in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        prevent the request from being sent if the user is not authenticated
    if(!auth.isAuthenticated() || (auth instanceof AnonymousAuthenticationToken)){
        throw new AuthenticationNotFoundException("You are not authenticated");
    }

        UserInfo user = userRegistration.findUserByPhone(auth.getName());
//        Now that you have the user: link the accounts
        Account account = Account.builder()
                .accountId(UUID.randomUUID().toString())
                .accountNumber(accountLink.getAccountNumber())
                .bankId(bank)
                .userId(user)
                .build();
        try{
            accountRepo.save(account);
        }catch(Exception e){
            throw new AccountLinkFailedException("The "+bank.getName()+" account number you submitted is already linked to an account");
        }
        return accountLink;


    }

    @Override
    public UserInfo findUserByAccountNumberAndBank(String acc, Bank bankid) {
        return accountRepo.findAccountsByAccountNumberAndBankId(acc, bankid).getUserId();
    }
}
