package com.group4.qrcodepayment.events.handler;

import com.group4.qrcodepayment.Repositories.QPayAccountRepo;
import com.group4.qrcodepayment.Repositories.TransactionRepo;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.events.event.TransactionRecordedEvent;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.Transactions;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.*;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TransactionRecordEventHandler {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private BankServiceImpl bankService;
    @Autowired
    private QPayAccountImpl qPayAccountService;
    @Autowired
    private TransactionServiceImpl transactionService;
    @Autowired
    private TwilioConfig twilioConfig;

    @EventListener
    @Async
    public void handleTransactionEvent(TransactionRecordedEvent recordedEvent){
//        Get the bank Object
        Bank bank = bankService.getBankById(recordedEvent.getBankCode());

//        This is the account holder
        UserInfo  userInfo = accountService.findUserByAccountNumberAndBank(recordedEvent.getAccountNumber(),bank);
//        Based on the userinfo received, fetch the account held by this user and update it accordingly
        QPayAccount qPayAccount = qPayAccountService.getQPayAccount(userInfo);

//      Fetch the transaction based on reference
        Transactions transaction = transactionService
                .getTransactionById(recordedEvent.getTransactionRef());
//        Update the account now that every info is available
        qPayAccount.setBalance(Integer
                .parseInt(transaction.getAmount()) + qPayAccount.getBalance());
//        Commit the transaction
        qPayAccountService.updateAccount(qPayAccount);
//        Now Send the SMS

        Message.creator(
                new PhoneNumber("+254"+userInfo.getPhone()),
                new PhoneNumber(twilioConfig.getTrialNumber()),
                transaction.getTransactionRef() + " Confirmed on "+ transaction.getDateTime().toString().split("T")[0]+""
                        +" at "+ transaction.getDateTime().toString().split("T")[1].split("\\.")[0]+
                        " Ksh"+transaction.getAmount()+"" +
                        " has been deposited to your QPay Account from your "+bank.getName()+" Bank Account XXXXXX" +
                        transaction.getSourceAccount().substring(transaction.getSourceAccount().length()-4)+
                        " New QPay Balance is KES"+ qPayAccount.getBalance()
        ).create();
        LoggerFactory.getLogger(this.getClass()).error(transaction.getDateTime().toString());
    }

}
