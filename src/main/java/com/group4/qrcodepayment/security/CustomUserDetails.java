package com.group4.qrcodepayment.security;

import com.group4.qrcodepayment.models.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class CustomUserDetails implements UserDetails {
    private final UserInfo user;
    public CustomUserDetails(UserInfo user){
        this.user = user;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Logger logger = LoggerFactory.getLogger(CustomUserDetails.class);


      Collection<SimpleGrantedAuthority> coll =  Arrays.stream(user.getRoles().split(" "))

              .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

                logger.info("The logged in user has the following Roles "+ coll);

      return coll;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
