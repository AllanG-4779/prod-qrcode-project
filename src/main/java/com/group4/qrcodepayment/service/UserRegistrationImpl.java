package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationImpl implements UserRegistrationService{
    @Autowired
    private UserRepoInt userRepo;
    @Override
    public UserInfo userRegister(UserInfo user) {
         return userRepo.save(user);

    }

    @Override
    public void checkUserNameExists(String username) throws PhoneOrEmailExistsException {
        UserInfo usr = userRepo.findByUsername(username);
        if (!(usr == null)){
            throw new PhoneOrEmailExistsException("Username is taken");
        }
    }

    @Override
    public void checkEmailExists(String email) throws PhoneOrEmailExistsException {
     UserInfo user = userRepo.findUserByEmail(email);
     if (!(user==null))
         throw new PhoneOrEmailExistsException("Email address is taken");
    }
}
