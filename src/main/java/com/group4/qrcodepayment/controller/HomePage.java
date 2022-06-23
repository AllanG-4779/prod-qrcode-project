package com.group4.qrcodepayment.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/insights")


@CrossOrigin(origins = "*")
public class HomePage {

    @GetMapping("/home")
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
