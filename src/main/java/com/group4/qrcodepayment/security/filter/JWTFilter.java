package com.group4.qrcodepayment.security.filter;

import com.group4.qrcodepayment.security.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        String username = null;
//        get the authorization
        String authorization = request.getHeader("QP-Authorization");

        if(authorization !=null && authorization.startsWith("Bearer")){
             token = authorization.split(" ")[1];

             username = jwtUtils.getUsernameFromToken(token);

        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
// In other words we are checking if there is a user in the token and that user is not yet
//            authenticated
//            if so, then the user is logged in
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

//            Now, validate the details in the token against the actual user with the said username
            if(jwtUtils.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,null,
                        userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

//            We are done now proceed with the next filter chains

        }
        filterChain.doFilter(request,response);
    }
}
