package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.boot.configurationprocessor.json.JSONException;

import java.sql.SQLException;

public interface UserRegistrationService {
    void userRegister(UserInfo user) throws JSONException;

    void checkUserNameExists(String username) throws PhoneOrEmailExistsException;

    void checkEmailExists(String email) throws PhoneOrEmailExistsException;

    Boolean numberRegistered(String phone) throws SQLException;

    void setAccountVerified(String phone);
}
