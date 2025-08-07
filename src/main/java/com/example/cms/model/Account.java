package com.example.cms.model;

import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false)
    private Status status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", updatable = false, nullable = false)
    private Currency currency;

    // Manually establishing a one-to-many relationship with the account_card join table:
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountCard> accountCards = new HashSet<>();

    public Account(Status status, BigDecimal balance, Currency currency){
        this.status = status;
        this.balance = balance;
        this.currency = currency;
    }


}
