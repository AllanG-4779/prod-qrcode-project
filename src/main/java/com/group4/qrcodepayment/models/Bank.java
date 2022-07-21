package com.group4.qrcodepayment.models;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Bank implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bankId;
    @Column(nullable = false, unique = true, updatable = false)
        private String ipslCode;
    @Column(nullable = false, unique = true, updatable = false)
        private String name;
    @Column(nullable = false)
    private  final Boolean supported=false;


}
