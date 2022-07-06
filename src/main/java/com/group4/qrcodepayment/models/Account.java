package com.group4.qrcodepayment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(AccountId.class)
public class Account {


    @Column(nullable = false)
    private String accountNumber;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
           referencedColumnName = "userId",
           name="user_Id", nullable = false
    )
    @Id
    private UserInfo userId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "ipslCode", name="bank_id", nullable = false)
    @Id
    private Bank bankId;
}
