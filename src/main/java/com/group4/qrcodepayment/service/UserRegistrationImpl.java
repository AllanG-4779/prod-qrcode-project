package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserRegistrationImpl implements UserRegistrationService{

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepoInt userRepo;
    @Override
    public void userRegister(UserInfo user) {
        userRepo.save(user);

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

    public Boolean numberRegistered(String number) throws SQLException {
        Optional<UserInfo> user;
        try{
             user =  userRepo.findUserInfoByPhone(number);
             logger.info("user found with these details "+ user);
        }catch( Exception e){
            throw new SQLException("Error executing query in verification to verify number");
        }

        return user.isPresent();
    }

    @Override
    public void setAccountVerified(String phone) {
        userRepo.verifyAccount(phone);
    }
}
