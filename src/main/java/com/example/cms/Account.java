package com.example.cms;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", updatable = true, nullable = false)
    private CardStatus status;

    @Column(name = "balance", updatable = true, nullable = true) // debatable whether nullable, will see about this.
    private BigDecimal balance;

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

}
