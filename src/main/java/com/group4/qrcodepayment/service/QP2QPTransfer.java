package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.Accounts.QP2QPTransactionResultDto;
import com.group4.qrcodepayment.dto.Accounts.Summary;
import com.group4.qrcodepayment.exception.InsuffientBalanceException;
import com.group4.qrcodepayment.exception.InvalidAmountException;
import com.group4.qrcodepayment.exception.RecipientNotFound;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.externals.coop.dto.InternalTransferDto;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class QP2QPTransfer {
    @Autowired
   private QPayAccountRepo qPayAccountRepo;
    @Autowired
   private UserRepoInt userRepo;
//    check if funds are sufficient
   public Boolean isFundsSufficient(int amount, QPayAccount account){
       return account.getBalance() >= amount;
   }

   public Boolean isAmountValid(int amount){
       return amount>0 && amount<=50000;
   }


   public UserInfo getLoggedInUser() throws AuthenticationNotFoundException {
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       if(!auth.isAuthenticated()  || auth instanceof AnonymousAuthenticationToken ){
           throw new AuthenticationNotFoundException("You must be authenticated");
       }
        UserInfo user =  userRepo.findByUsername(auth.getName());
        assert  user.getPhone() != null;

        return user;




   }

   public QPayAccount getSenderAccount() throws AuthenticationNotFoundException {
//       Currently logged-in user;
     UserInfo user = getLoggedInUser();

     return qPayAccountRepo.findQPayAccountByUserId(user);
   }
@Transactional(rollbackOn=InsuffientBalanceException.class)
    public QP2QPTransactionResultDto Transfer(InternalTransferDto transferDto) throws AuthenticationNotFoundException, InsuffientBalanceException, RecipientNotFound, InvalidAmountException {
        //       credit the account holder
//                 is balance credible and amount valid
//Ensure that the person is logged in
        UserInfo myUser = getLoggedInUser();
        if (myUser.getPhone().equals( transferDto.getReceiver())){
            throw new RecipientNotFound("You cannot transfer money to your own account");
        }
        if(!isAmountValid(Integer.parseInt(transferDto.getAmount()))){
            throw new InvalidAmountException("Minimum amount required is KES 1");
        }
//Check if there's a logged in user



        if (isAmountValid(Integer.parseInt(transferDto.getAmount())) &&
                (isFundsSufficient(Integer.parseInt(transferDto.getAmount()), getSenderAccount()))) {
            //              now do the crediting
            UserInfo receiver;
            try{
                receiver = userRepo.findByUsername(transferDto.getReceiver());
            }
          catch(NullPointerException ex){
                throw new RecipientNotFound("The receiver is not registered on QPay");
          }

            qPayAccountRepo.updateBalance(Integer.parseInt(transferDto.getAmount()), receiver);

            //              Debit the senders account

            qPayAccountRepo.updateBalance(-Integer.parseInt(transferDto.getAmount()), getLoggedInUser());


        } else {

            throw new InsuffientBalanceException("Insufficient account balance to complete the transaction");

        }
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
        return QP2QPTransactionResultDto.builder()
                .transactionRef("INT"+new RandomGenerator().generateRandom())
                .transactionTime(LocalDateTime.now().format(formatter))
                .sender(Summary.builder()
                        .afterBal("0")
                        .beforeBal("0")
                        .build())
                .receiver(Summary.builder()
                        .afterBal("0")
                        .beforeBal("0")
                        .build())
                .amount(transferDto.getAmount())


                .build();

    }




}
