package com.example.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_card")
public class AccountCard {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private UUID id; // pk

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false) // Foreign key to Account entity
    private Account account;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false) // Foreign key to Card entity
    private Card card;

    public AccountCard(Account account, Card card) {
        this.account = account;
        this.card = card;
    }
}
