package com.group4.qrcodepayment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table( uniqueConstraints = @UniqueConstraint(name="accountOwner", columnNames = {"userId", "bankId"}))
public class Account  {
    @Id
    private String accountId;
    @Column(nullable = false)
    private String accountNumber;

    @ManyToOne(
            fetch = FetchType.EAGER
    )
    @JoinColumn(
           referencedColumnName = "userId",
           name="userId", nullable = false
    )

    private UserInfo userId;
    @ManyToOne
    @JoinColumn(referencedColumnName = "ipslCode", name="bankId", nullable = false)

    private Bank bankId;
}
