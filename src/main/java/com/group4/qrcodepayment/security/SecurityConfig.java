package com.group4.qrcodepayment.security;

import com.group4.qrcodepayment.exception.resterrors.InvalidUsernameOrPasswordException;
import com.group4.qrcodepayment.security.filter.JWTFilter;
import com.group4.qrcodepayment.util.URLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.event.AuthenticationCredentialsNotFoundEvent;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
      private JWTFilter jwtFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
// this method is used to enforce security to various routes

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.cors().and().csrf().disable()
               .authorizeRequests()
               .antMatchers(URLs.HOME).authenticated()
               .antMatchers(URLs.LOGIN,URLs.REGISTER,
                       "/auth/registration/verify").permitAll()
//Tell the security that you don't want to use session based
               .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

//                                       .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
//               Add the filter




    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

           auth.authenticationProvider(authenticationProvider());
//           auth.authenticationEventPublisher(authenticationCredentialsNotFoundEvent());

    }
////    This is the authentication provider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);
//        dao.setHideUserNotFoundExceptions(false);
        return dao;


    }
//    What if the user failed to login


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
