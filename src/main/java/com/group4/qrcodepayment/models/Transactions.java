package com.group4.qrcodepayment.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account_transaction")
public class Transactions {
    @Id
    private String transactionRef;
    @Column(nullable = false)
    private String sourceAccount;
    @Column(nullable = false)
    private String destinationAccount;
    @Column(nullable = false)
    private String amount;
    private String status="Pending";
    @ManyToOne
    @JoinColumn(name="transaction_type", referencedColumnName = "transactionId", nullable = false)
    private TransactionType transactionType;
    @OneToOne
    @JoinColumn(name="transacting_object" , referencedColumnName = "userId",nullable = false)
    private UserInfo userId;

    private LocalDateTime dateTime;


}
