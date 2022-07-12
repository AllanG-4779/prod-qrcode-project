package com.group4.qrcodepayment.models;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bank {
    @Id
    @Column(nullable = false)
        private String ipslCode;
    @Column(nullable = false, unique = true)
        private String name;
    @Column(nullable = false)
    private  final Boolean supported=false;

}
