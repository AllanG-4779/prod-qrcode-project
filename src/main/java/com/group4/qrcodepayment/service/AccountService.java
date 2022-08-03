package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.Accounts.AccountLinkingDto;
import com.group4.qrcodepayment.dto.Accounts.TransactionResultDto;
import com.group4.qrcodepayment.dto.Accounts.TransferRequestDto;
import com.group4.qrcodepayment.exception.InsuffientBalanceException;
import com.group4.qrcodepayment.exception.RecipientNotFound;
import com.group4.qrcodepayment.exception.resterrors.*;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;

public interface AccountService {
    String getUserAccountNumber() throws BankLinkedException;
    AccountLinkingDto linkAccount(AccountLinkingDto accountLinkingDto) throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException;

    UserInfo findUserByAccountNumberAndBank(String acc, Bank bankid);
//  QR payment method
//  Direct Payment method
    TransactionResultDto sendAmountToUser(TransferRequestDto requestDto) throws AuthenticationNotFoundException, InsuffientBalanceException, RecipientNotFound, TransactionNotFoundException;

}
