package com.example.cms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Getter
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false)
    private Status status;

    @Getter @Setter
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", updatable = false, nullable = false)
    private Currency currency;

    @ManyToMany
    @JoinTable(
            name = "account_card",
            joinColumns = @JoinColumn(name="account_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<Card> cards;

    // No arg constructor JPA uses when retrieving info from the db
    public Account(){
        this.cards = new HashSet<>(); // As far as I know, this is optional.
        // TO-DO: add debug message
    }

    // Parameterized constructor
    public Account(Status status, BigDecimal balance, Currency currency){
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.cards = new HashSet<>();
    }


}
