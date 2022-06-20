package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;

public interface UserRegistrationService {
    UserInfo userRegister(UserInfo user);

    void checkUserNameExists(String username) throws PhoneOrEmailExistsException;

    void checkEmailExists(String email) throws PhoneOrEmailExistsException;
}
