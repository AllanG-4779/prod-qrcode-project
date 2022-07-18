package com.group4.qrcodepayment.externals.mpesa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MpesaControllers {
    @Autowired
    private Config mpesaConfig;

    private  Logger log;
    @GetMapping("/mpesa/token")
    public MpesaToken getToken(){
        return mpesaConfig.getToken();
    }
    @GetMapping("/mpesa/register")
    public Object register() throws JsonProcessingException {
        return mpesaConfig.registerUrl();
    }
    @PostMapping("/mpesa/transfer")
    public Object fundQPay() throws JsonProcessingException {
        return mpesaConfig.fundAccountViaMpesa();
    }
    @PostMapping("/v1/confirmation")

    public void confirmationUrl(@RequestBody Object req){
      log.error(req.toString());
    }
    @PostMapping("/v1/validation")
    public  void validationUrl(@RequestBody String req){
        log.info(req);
    }
}
