package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.exception.resterrors.RegistrationFailedException;
import com.group4.qrcodepayment.models.UserInfo;

import javax.swing.text.StyledEditorKit;
import java.sql.SQLException;

public interface UserRegistrationService {
    void userRegister(UserInfo user) throws  RegistrationFailedException;

    void checkUserNameExists(String username) throws PhoneOrEmailExistsException;

    void checkEmailExists(String email) throws PhoneOrEmailExistsException;

    Boolean numberRegistered(String phone) throws SQLException;

    void setAccountVerified(String phone);

    UserInfo findUserByPhone(String phone);

    void updateUserpassword(String phone, String password);
    void flagAccount(String userId, Boolean flag);

   // UserInfo findUserByAccountAndBank(String accountNumber, String bankId);
}
