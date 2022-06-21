package com.group4.qrcodepayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpDto {
    private Long Id;
    private String code;
    private String owner;
    private LocalDateTime issueAt;
    private LocalDateTime expireAt;
}
