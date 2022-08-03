package com.group4.qrcodepayment.dto.Accounts;

import com.group4.qrcodepayment.models.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QpayAccountDto {

    private UserInfo userInfo;
    private Integer amount ;

}
