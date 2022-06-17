package com.group4.qrcodepayment.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;

/*
This controller is responsible for handling any error that will be encountered
and render the path instead of the white label error page shown on the browser
 */
@Controller
public class ApiErrorController implements ErrorController {

    public String getErrorPath(){
        return "/error";
    }
/*
Here you are saying that any request mapped to the error path, an exception should be thrown
The exceptions thrown handles every form of exception status codes i.e. 400 and 500 codes.
 */
    @RequestMapping("/error")
    public void globalError(HttpServletResponse response){
        throw new ResponseStatusException(HttpStatus.valueOf(response.getStatus()));
    }


}
