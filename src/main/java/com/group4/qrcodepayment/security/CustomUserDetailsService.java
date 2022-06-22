package com.group4.qrcodepayment.security;

import com.group4.qrcodepayment.Repositories.UserRepoInt;
import com.group4.qrcodepayment.models.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepoInt repoInt;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //set a logger
        Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

        UserInfo user = repoInt.findUserByPhoneOREmail(email);

        logger.info( "The user is "+ user);


        if (user == null){
            logger.info("user was not successfully authenticated ");
            throw new UsernameNotFoundException("No such user in the database");
        }
        return new CustomUserDetails(user);
    }
}
