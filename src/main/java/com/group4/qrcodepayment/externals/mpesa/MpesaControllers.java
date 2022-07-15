package com.group4.qrcodepayment.externals.mpesa;

import com.group4.qrcodepayment.externals.mpesa.dto.MpesaToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MpesaControllers {
    @Autowired
    private Config mpesaConfig;

    @GetMapping("/mpesa/token")
    public MpesaToken getToken(){
        return mpesaConfig.getToken();
    }
}
