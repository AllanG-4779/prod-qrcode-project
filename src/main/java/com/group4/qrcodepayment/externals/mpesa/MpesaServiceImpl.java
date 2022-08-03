package com.group4.qrcodepayment.externals.mpesa;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.externals.mpesa.dto.TransactionMetadata;
import com.group4.qrcodepayment.models.Transactions;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.QPayAccountImpl;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpesaServiceImpl implements MpesaService{

 @Autowired
 private TransactionServiceImpl transactionService;
  @Autowired
 private QPayAccountImpl qPayAccount;
  @Autowired
  private UserRegistrationImpl userRegistration;
  @Autowired
  private TwilioConfig twilioConfig;
    @Override
    public void updateTransaction(String transactionId, String status) {
       Transactions transaction =  transactionService.getTransactionById(transactionId);
        transactionService.completeTransaction(transaction.getTransactionRef(), status);
    }

    @Override
    public void updateUser(TransactionMetadata transactionMetadata) {

        String phone = transactionMetadata.getPhoneNumber().toString().substring(3);
        UserInfo user = userRegistration.findUserByPhone(phone);
        assert user!=null;

        Message.creator(
                new PhoneNumber("+"+transactionMetadata.getPhoneNumber().toString()),
                new PhoneNumber(twilioConfig.getTrialNumber()),
                "Dear Customer, KES "+transactionMetadata.getAmount()+" transfered" +
                        " from your Mpesa account to QPay account. New account balance is " +
                        "KES "+ qPayAccount.getQPayAccount(user).getBalance()
        ).create();
    }
}
