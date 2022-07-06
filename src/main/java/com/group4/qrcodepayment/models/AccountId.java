package com.group4.qrcodepayment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//this class acts as a primary to key to the accounts model
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountId implements Serializable {
    private UserInfo userId;
    private Bank bankId;
}
