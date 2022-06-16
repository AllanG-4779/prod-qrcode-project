package com.group4.qrcodepayment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePage {

    @GetMapping("/")
    public String testUser(){
        return "The security is enforced successfully";
    }

}
