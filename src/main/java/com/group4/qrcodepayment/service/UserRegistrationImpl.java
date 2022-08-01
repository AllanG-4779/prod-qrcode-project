package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.exception.resterrors.PhoneOrEmailExistsException;
import com.group4.qrcodepayment.exception.resterrors.RegistrationFailedException;
import com.group4.qrcodepayment.models.Bank;
import com.group4.qrcodepayment.models.QPayAccount;
import com.group4.qrcodepayment.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRegistrationImpl implements UserRegistrationService{

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserRepoInt userRepo;
    @Autowired
    private  QPayAccountImpl qPayAccountService;
    @Override
    public void userRegister(UserInfo user) throws RegistrationFailedException {
        try{
            userRepo.save(user);
//            initialize the registration
            QPayAccount qPayAccount = QPayAccount.builder()
                    .accountId(UUID.randomUUID().toString().toLowerCase())
                    .balance(0)
                    .userId(user)
                    .build();
            qPayAccountService.addNewAccount(qPayAccount);


        }
        catch(Exception e){
           throw new RegistrationFailedException(e.getLocalizedMessage());
        }



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

    @Override
    public UserInfo findUserByPhone(String phone) {
        try{
        return userRepo.findByUsername(phone);
        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    @Override
    public void updatePassword( String phone, String password) {
        userRepo.updatePassword(phone ,password);
    }

    // @Override
//    public UserInfo findUserByAccountAndBank(String accountNumber, Bank bankId) {
//        return userRepo.findUserByBankIdAndAccountNumber(accountNumber, bankId);
//    }

}
