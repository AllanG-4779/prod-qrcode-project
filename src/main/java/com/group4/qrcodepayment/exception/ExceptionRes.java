package com.group4.qrcodepayment.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionRes {
    private Integer code;
    private String message;
    private String debugMessage;
}
