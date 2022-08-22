package com.group4.qrcodepayment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class SecQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String question;
    @ManyToMany()
    @JoinTable(
            name = "user_question"
    )
    private List<UserInfo> userId;
}
