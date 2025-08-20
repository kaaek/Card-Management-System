package com.areeba.cms.account.model;

import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", updatable = false, nullable = false)
    private Currency currency;

    public Account(Status status, BigDecimal balance, Currency currency){
        this.status = status;
        this.balance = balance;
        this.currency = currency;
    }


}
