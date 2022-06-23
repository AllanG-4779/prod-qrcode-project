package com.group4.qrcodepayment.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/insights")
@CrossOrigin(origins = "*")
public class HomePage {

    @GetMapping("/")
    public String testUser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null){
            return "Welcome " + auth.getName()+ " to our app";

        }
        else{
            return "You are not authenticated ";
        }

    }

}
