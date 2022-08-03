package com.group4.qrcodepayment.controller;

import com.google.zxing.WriterException;
import com.group4.qrcodepayment.Repositories.BankRepo;
import com.group4.qrcodepayment.Repositories.TransactionRepo;
import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.dto.Accounts.*;
import com.group4.qrcodepayment.exception.InsuffientBalanceException;
import com.group4.qrcodepayment.exception.RecipientNotFound;
import com.group4.qrcodepayment.exception.resterrors.AccountLinkFailedException;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.UnsupportedBankException;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.service.QPayAccountImpl;
import com.group4.qrcodepayment.util.QRCodeGenerator;
import lombok.AllArgsConstructor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/acc")
@CrossOrigin(origins = "*")
public class Accounts {

    private BankRepo repo;

    private UserRepoInt userRepoInt;
    private AccountServiceImpl accountService;
    private QPayAccountImpl qPayAccountService;
    private TransactionRepo transactionRepo;
    @PostMapping("/link")
    public AccountLinkingDto linkAccount(@RequestBody @Valid AccountLinkingDto accountLink)
            throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException {


        return accountService.linkAccount(accountLink);

    }


    @GetMapping("/myqr-code")
    public byte[] generateMyQr(@RequestParam int width, @RequestParam int  height,
                               @RequestParam(required = false) Integer amount) throws IOException, WriterException, AuthenticationNotFoundException {
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
        String message = "name="+user.getFirstName()+user.getSecondName()+ " phone="+user.getPhone()+" amount="+amount;
        String hash = textEncryptor.encrypt(message);


          return     QRCodeGenerator.getQRcodeImage(hash,width,height);



    }
  @GetMapping("/home")
    private HomeDto homePage() throws AuthenticationNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)
        ){
//            Get the user
            UserInfo user = userRepoInt.findByUsername(authentication.getName());
            QPayAccount qPayAccount = qPayAccountService.getQPayAccount(user);
           List<TransactionListDto> transactionList =
                   transactionRepo.findAllByUserId(user)
                           .stream().map(transactions -> TransactionListDto
                                   .builder()
                                   .amount(transactions.getAmount())
                                   .destinationAccount(transactions.getDestinationAccount())
                                   .sourceAccount(transactions.getSourceAccount())
                                   .transactionRef(transactions.getTransactionRef())
                                   .dateTime(transactions.getDateTime())
                                   .status(transactions.getStatus())
                                   .transactionType(transactions.getTransactionType().getTransactionId())
                                   .build()).collect(Collectors.toList());
           return HomeDto.builder()
                   .qpayBalance(qPayAccount.getBalance())
                   .transactionLists(transactionList)
                   .userId(user.getPhone())
                   .build();



        }
        else{
            throw new AuthenticationNotFoundException("Authentication is required, Please login");
        }


  }
//  To pay, we need the token in the QR code
    @PostMapping("/secure/transfer")
    public TransactionResultDto transferCash(@RequestParam(required = false) String paymentHash) throws AuthenticationNotFoundException, InsuffientBalanceException, RecipientNotFound, TransactionNotFoundException {
/*
 *  The payment hash should contain the source of funds
 *  The amount to be paid
 *  Who is to be paid
 *  In case of bank, please pass the bank code, QPAY for internal transfer and MPESA for mpesa
 *  source
**/
//        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
//        textEncryptor.setPassword("Cnd80751xh.10@27.com");
//        String paymentDetails =  textEncryptor.decrypt(paymentHash);
//        String[] details = paymentDetails.split(" ");
//        OurRequest

//        return TransferRequestDto.builder()
//                .recipientPhone(details[0].split("=")[1])
//                .source(details[1].split("=")[1])
//                .amount(Integer.parseInt(details[2].split("=")[1]))
//                .build();
        TransferRequestDto resultDto = TransferRequestDto.builder()
                .recipientPhone("747407365")
                .source("QPAY")
                .amount(900)
                .build();

      return  accountService.sendAmountToUser(resultDto);



    }

}
