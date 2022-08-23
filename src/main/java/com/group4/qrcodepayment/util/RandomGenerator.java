package com.group4.qrcodepayment.util;

import com.group4.qrcodepayment.config.TwilioConfig;
import com.group4.qrcodepayment.dto.Accounts.TransactionDto;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import com.group4.qrcodepayment.service.QPayAccountImpl;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.xml.crypto.Data;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

@Component
@Service
public class RandomGenerator {
    private static final int LENGTH =  8;
    @Autowired
    private QPayAccountImpl qPayAccountService;
    @Autowired
    private TwilioConfig config;

    public String  generateRandom(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123458679";

        Random rand = new Random();
        char[] letter = new char[LENGTH];

        for ( int i =0; i<LENGTH; i++){
            letter[i] = characters.charAt(rand.nextInt(characters.length()-1));
        }
        String finalResult = Arrays.toString(letter);
        return finalResult.replaceAll("\\[","")
                .replaceAll("\\]","")
                .replaceAll(" ","")
                .replaceAll(",","")
                .toUpperCase();
    }
//    This method prevents SSL checks
    public  HttpComponentsClientHttpRequestFactory  disableSsL() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }
    public String nameFormatter(UserInfo user){
        return user.getFirstName() + " "+ user.getSecondName();
    }
//    send SMS upon successful internal transaction
          public void SendDualSMS(UserInfo sender, UserInfo recipient,TransactionDto receiverDto, TransactionDto senderDto){
          DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
          DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm a");
          QPayAccount senderAccount = qPayAccountService.getQPayAccount(sender);
          QPayAccount receiverAccount = qPayAccountService.getQPayAccount(recipient);
          Message.creator(
              new PhoneNumber("+254"+sender.getPhone()),
              new PhoneNumber(config.getTrialNumber()),
                  senderDto.getTransactionRef() + " confirmed on "+ senderDto.getDate().plusHours(3).format(date) + " " +
                          " at "+ senderDto.getDate().format(time)+ ". Ksh "+ senderDto.getTransactionAmount()+ "" +
                          " sent to "+ nameFormatter(recipient) + ". New QPay Account balance is "+"Ksh " +
                          (senderAccount.getBalance()-(Integer.parseInt(senderDto.getTransactionAmount())))
          ).create();
//          Send to the receiver

          Message.creator(
                      new PhoneNumber("+254"+recipient.getPhone()),
                      new PhoneNumber(config.getTrialNumber()),
                      receiverDto.getTransactionRef() + " confirmed on "+ receiverDto.getDate().plusHours(3).format(date) + " " +
                              " at "+ receiverDto.getDate().format(time)+ ". You have received Ksh "+ receiverDto.getTransactionAmount()+ " " +
                              "from "+ nameFormatter(sender) + ". New QPay Account balance is "+"Ksh " +
                              (receiverAccount.getBalance()-(Integer.parseInt(receiverDto.getTransactionAmount())))
              ).create();
      }
    }



