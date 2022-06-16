package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.exception.resterrors.UsernameOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;

public interface UserRegistrationService {
    UserInfo userRegister(UserInfo user);

    void checkUserNameExists(String username) throws UsernameOrEmailExistsException;

    void checkEmailExists(String email) throws  UsernameOrEmailExistsException;
}
