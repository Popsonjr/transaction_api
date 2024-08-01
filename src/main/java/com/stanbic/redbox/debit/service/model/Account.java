package com.stanbic.redbox.debit.service.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_Accounts_seq")
    @SequenceGenerator(name = "User_Accounts_seq", sequenceName = "User_Accounts_seq", allocationSize = 1)
    private Long id;

//    @Column(name = "account_number")
    private String accountNumber;

//    @Column(name = "account_name")
    private String accountName;

    private BigDecimal balance;
}
