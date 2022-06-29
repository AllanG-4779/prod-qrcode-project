package com.group4.qrcodepayment.externals.coop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TransferResDto {
    private String MessageReference;
    private LocalDateTime  MessageDateTime;
    private String MessageCode;
    private String MessageDescription;
}
