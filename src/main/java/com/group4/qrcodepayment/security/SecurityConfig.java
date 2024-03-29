package com.group4.qrcodepayment.security;

import com.group4.qrcodepayment.security.filter.JWTFilter;
import com.group4.qrcodepayment.util.URLs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
               .antMatchers(URLs.MPESA_FUND_ACCOUNT,URLs.TRANSFER, URLs.ACCOUNT_LINK).hasRole("USER")
               .antMatchers(URLs.HOME, URLs.MPESA_TOKEN, URLs.MPESA_REGISTER).hasAnyRole("USER","ADMIN")
               .antMatchers("/acc/**").authenticated()
               .antMatchers(URLs.LOGIN,URLs.REGISTER,URLs.SEND_OTP,
                URLs.ASSERT_REGISTRATION, URLs.VALIDATE_OTP, "/externals/coop/transfer/result", URLs.MPESA_CONFIRMATION,
       URLs.MPESA_VALIDATION).permitAll()
                       .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
