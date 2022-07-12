package com.group4.qrcodepayment.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false)
    private Long userId;
    @Column(nullable = false, length = 30)
    private String firstName;
    @Column(nullable=false, length = 30)
    private String secondName;
    @Column(unique = true, nullable = false)
    private String idNo;
    @Column(unique = true, updatable = false)
    private String email;
    @Column(unique = true, updatable = false)
    private String phone;

    private String password;
    private String roles = "USER";
    private boolean isConfirmed = false;
//    A user can have many roles;

}
