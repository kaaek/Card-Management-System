package com.example.cms.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Data
@Table(name = "account_card")
public class AccountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id; // pk

    @ManyToOne
    @Getter @Setter
    @JoinColumn(name = "account_id", nullable = false) // Foreign key to Account entity
    private Account account;

    @ManyToOne
    @Getter @ Setter
    @JoinColumn(name = "card_id") // Foreign key to Card entity
    private Card card;
}
