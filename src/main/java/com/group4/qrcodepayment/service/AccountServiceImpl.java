package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.AccountRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.Accounts.*;
import com.group4.qrcodepayment.exception.InsuffientBalanceException;
import com.group4.qrcodepayment.exception.RecipientNotFound;
import com.group4.qrcodepayment.exception.resterrors.*;
import com.group4.qrcodepayment.models.Account;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.util.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
   private TwilioConfig twilioConfig;

    @Autowired
    private UserRegistrationImpl userRegistration;
    @Autowired
    private QPayAccountImpl qPayAccount;
    @Autowired
    private TransactionServiceImpl transactionService;

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

    @Override
    public TransactionResultDto sendAmountToUser(TransferRequestDto requestDto) throws AuthenticationNotFoundException, InsuffientBalanceException, RecipientNotFound, TransactionNotFoundException {
//          Get the source account
//          The transaction happens by default from the sender's QPAY account
//        Assert authentication is available
        TransactionResultDto transactionResultDto;
        Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)){
            throw new AuthenticationNotFoundException("To send money, please authenticate yourself");
        }else{
//          Check for sufficient funds in the account
            UserInfo sender = userRegistration
                    .findUserByPhone(authentication.getName());
            QPayAccount senderQPay = qPayAccount.getQPayAccount(sender);

            int balance = senderQPay.getBalance();
            if(balance<requestDto.getAmount()){
                throw new InsuffientBalanceException("Not enough money in your account to transfer "+requestDto.getAmount());

            }else{
//              check for existence of the user in the registered database
//                Get the user
                UserInfo actualRecipient = userRegistration
                        .findUserByPhone(requestDto.getRecipientPhone());
//                Get the account to be credited
                QPayAccount recipientAccount = qPayAccount
                        .getQPayAccount(actualRecipient);
                if(recipientAccount == null){
                    throw new RecipientNotFound("Account to be credited not found");
                }
//                credit the account
                qPayAccount.updateAccount(requestDto.getAmount(),actualRecipient);
//                debit the senders account
                qPayAccount.updateAccount(requestDto.getAmount()*-1, sender);



                transactionResultDto=TransactionResultDto.builder()
                        .amount(requestDto.getAmount())
                        .dateTime(LocalDateTime.now())
                        .transactionRef(new RandomGenerator().generateRandom())
                        .receiver(QPayReceiver.builder()
                                .account(requestDto.getRecipientPhone())
                                .amount(requestDto.getAmount())
                                .fullName(actualRecipient.getFirstName()+" "+actualRecipient.getSecondName())
                                .transactionRef(new RandomGenerator().generateRandom())
                                .transactionType('D')
                                .build())
                        .status("COMPLETED")
                        .sender(sender.getPhone())

                        .transactionType('W').build();

//                Save the debit transaction
                transactionService.addTransaction(
                        TransactionDto.builder()
                                .transactionAmount(transactionResultDto.getAmount().toString())
                                .destinationAccount(requestDto.getRecipientPhone())
                                .sourceAccount(transactionResultDto.getSender())
                                .date(transactionResultDto.getDateTime())
                                .status(transactionResultDto.getStatus())
                                .userId(sender)
                                .transactionRef(transactionResultDto.getTransactionRef())
                                .transactionType(transactionResultDto.getTransactionType())
                                .build());
//                Save the debit
                transactionService.addTransaction(
                        TransactionDto.builder()
                                .transactionAmount(transactionResultDto.getReceiver().getAmount().toString())
                                .destinationAccount(requestDto.getRecipientPhone())
                                .sourceAccount(transactionResultDto.getSender())
                                .date(transactionResultDto.getDateTime())
                                .status(transactionResultDto.getStatus())
                                .userId(userRegistration.findUserByPhone(transactionResultDto.getReceiver().getAccount()))
                                .transactionRef(transactionResultDto.getReceiver().getTransactionRef())
                                .transactionType(transactionResultDto.getReceiver().getTransactionType())
                                .build());
                // time formatter
                DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");

                //        SEND SMS TO SENDER
//                Parameters required
                LocalDateTime senderDate= transactionResultDto.getDateTime();



                String body = transactionResultDto.getTransactionRef() + " Confirmed on " +senderDate.format(date)+" at "+senderDate.format(time)+". Ksh "+transactionResultDto.getAmount()+"" +
                        " sent to "+transactionResultDto.getReceiver().getFullName()+" "+transactionResultDto.getReceiver().getAccount()+"." +
                        " New QPay account balance is "+ (senderQPay.getBalance()-transactionResultDto.getAmount());
                String receiverBody = transactionResultDto.getReceiver().getTransactionRef() + " Confirmed on " + senderDate.format(date) +" at " +senderDate.format(time)+" "+"You have received Ksh "+transactionResultDto.getAmount()+"" +
                        " from "+sender.getFirstName()+" " +sender.getSecondName()+" "+transactionResultDto.getSender()+"" +
                        " New QPay account balance is Ksh. "+ (recipientAccount.getBalance()+transactionResultDto.getAmount());

            twilioConfig.sendSMS(twilioConfig.getTrialNumber(), transactionResultDto.getSender(), body);
                twilioConfig.sendSMS(twilioConfig.getTrialNumber(), transactionResultDto.getReceiver().getAccount(), receiverBody);
            }




        }
    return transactionResultDto;
    }
}
