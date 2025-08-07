package com.example.cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "account_card")
public class AccountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id; // pk

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false) // Foreign key to Account entity
    private Account account;

    @ManyToOne
    @JoinColumn(name = "card_id") // Foreign key to Card entity
    private Card card;

    public AccountCard(Account account, Card card) {
        this.account = account;
        this.card = card;
    }
}
