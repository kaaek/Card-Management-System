package com.example.cms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
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
    private CardStatus status;

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

}
