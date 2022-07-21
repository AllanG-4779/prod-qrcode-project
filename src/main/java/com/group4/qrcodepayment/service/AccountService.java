package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.dto.AccountLinkingDto;
import com.group4.qrcodepayment.exception.resterrors.AuthenticationNotFoundException;
import com.group4.qrcodepayment.exception.resterrors.AccountLinkFailedException;
import com.group4.qrcodepayment.exception.resterrors.BankLinkedException;
import com.group4.qrcodepayment.exception.resterrors.UnsupportedBankException;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.UserInfo;

public interface AccountService {
    String getUserAccountNumber() throws BankLinkedException;
    AccountLinkingDto linkAccount(AccountLinkingDto accountLinkingDto) throws UnsupportedBankException, AccountLinkFailedException, AuthenticationNotFoundException;

    UserInfo findUserByAccountNumberAndBank(String acc, Bank bankid);
}
