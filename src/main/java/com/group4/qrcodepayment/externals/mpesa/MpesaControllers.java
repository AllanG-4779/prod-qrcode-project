package com.group4.qrcodepayment.externals.mpesa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import com.group4.qrcodepayment.externals.mpesa.dto.TransactionMetadata;
import com.group4.qrcodepayment.models.Transactions;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.QPayAccountImpl;
import com.group4.qrcodepayment.service.TransactionServiceImpl;
import com.group4.qrcodepayment.service.UserRegistrationImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;


import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MpesaControllers {
    @Autowired
    private Config mpesaConfig;
    @Autowired
    private MpesaServiceImpl mpesaService;
    @Autowired
    private TwilioConfig twilioConfig;
    @Autowired
    private QPayAccountImpl qpayService;
    @Autowired
    private UserRegistrationImpl userRegistration;
    private  Logger log;
    @Autowired
    private TransactionServiceImpl transactionService;
    @GetMapping("/mpesa/token")
    public MpesaToken getToken(){
        return mpesaConfig.getToken();
    }
    @GetMapping("/mpesa/register")
    public Object register() throws JsonProcessingException {
        return mpesaConfig.registerUrl();
    }
    @PostMapping("/mpesa/transfer")
    public Object fundQPay(@RequestParam String amount) throws JsonProcessingException, AuthenticationNotFoundException, TransactionNotFoundException {
        return mpesaConfig.fundAccountViaMpesa(amount);
    }
    @PostMapping("/v1/confirmation")

    public void confirmationUrl(@RequestBody Map<Object, Object> res) throws JSONException {
     JSONObject body = new JSONObject((Map<?,?>) res.get("Body"));

     JSONObject mpesaRes = body.getJSONObject("stkCallback");
     System.out.println(mpesaRes);
     String status;
     JSONObject medaData;

     String transactionId =  mpesaRes.get("CheckoutRequestID").toString();
//       check if the transaction was successful
        if (Integer.parseInt(mpesaRes.get("ResultCode").toString())==0){
//            the transaction was successful
            status = "Completed";
            medaData = mpesaRes.getJSONObject("CallbackMetadata");
            JSONArray jsonArray =(JSONArray) medaData.get("Item");
            JSONObject amount =  jsonArray.getJSONObject(0);
            JSONObject receipt = jsonArray.getJSONObject(1);
            JSONObject phone = jsonArray.getJSONObject(3);

            TransactionMetadata transactionMetadata = new TransactionMetadata();
            transactionMetadata.setAmount(amount.getInt("Value"));
            transactionMetadata.setReceipt(receipt.getString("Value"));
            transactionMetadata.setPhoneNumber(phone.getLong("Value"));
            System.out.println(transactionMetadata);
//            get the account owner
            String phoneNumber = transactionMetadata.getPhoneNumber().toString().substring(3);
            UserInfo userToCredit = userRegistration.findUserByPhone(phoneNumber);
            qpayService.updateAccount(transactionMetadata.getAmount(),userToCredit);


            mpesaService.updateTransaction(transactionId, status);
            mpesaService.updateUser(transactionMetadata);


        }else{

            status = "Cancelled";
            mpesaService.updateTransaction(transactionId, status);
//            Find the user
           Transactions transactions= transactionService.getTransactionById(transactionId);
           UserInfo user = transactions.getUserId();

            Message.creator(
                    new PhoneNumber("+254"+user.getPhone()),
                    new PhoneNumber(twilioConfig.getTrialNumber()),
                    "Dear customer, you cancelled the request to fund your QPay " +
                            "account via Mpesa.\n" +
                            "Please try again if this was a mistake."
            ).create();
        }

    }
    @PostMapping("/v1/validation")
    public  void validationUrl(@RequestBody String req){
        log.info(req);
    }
    @GetMapping("/get")
    public UserInfo getUser(){
        return userRegistration.findUserByPhone("796407365");
    }

}
