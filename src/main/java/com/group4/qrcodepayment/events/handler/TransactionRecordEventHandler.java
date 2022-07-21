package com.group4.qrcodepayment.events.handler;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.events.event.TransactionRecordedEvent;
import com.group4.qrcodepayment.externals.coop.dto.response.TransferResponse;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.Transactions;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.AccountServiceImpl;
import com.group4.qrcodepayment.service.BankServiceImpl;
import com.group4.qrcodepayment.service.QPayAccountImpl;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        TransferResponse transferResponse = recordedEvent.getTransferResponse();
//        Get the bank Object
        Bank bank = bankService.getBankByIpslCode(transferResponse
                .destinations.get(0).BankCode);

//        This is the account holder
        UserInfo  userInfo = accountService
                .findUserByAccountNumberAndBank(transferResponse.source.AccountNumber,bank);
//        Update the account by the balance
        qPayAccountService.updateAccount(Integer.parseInt(transferResponse.source.Amount), userInfo);
        QPayAccount qpay = qPayAccountService.getQPayAccount(userInfo);



//        update the transaction as completed
        Transactions  currentTransaction = transactionService
                .getTransactionById(transferResponse.messageReference);
        currentTransaction.setCompleted(true);
        transactionService.completeTransaction(currentTransaction);
//        Now Send the SMS
        Message.creator(
                new PhoneNumber("+254"+userInfo.getPhone()),
                new PhoneNumber(twilioConfig.getTrialNumber()),
                transferResponse.destinations.get(0).ReferenceNumber + " Confirmed on "+ LocalDate.now()
                        +" at "+LocalDateTime.now().toString().split("T")[1]+
                        " Ksh"+transferResponse.source.Amount+" " +
                        "has been deposited to your QPay Account from your "+bank.getName()+" Bank Account XXXXXX" +
                        transferResponse.source.AccountNumber.substring(transferResponse.source.AccountNumber.length()-4)+
                        " New QPay Balance is KES"+ qpay.getBalance()
        ).create();

    }

}
