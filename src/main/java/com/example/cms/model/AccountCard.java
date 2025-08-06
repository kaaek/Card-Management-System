package com.example.cms.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "account_card")
public class AccountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id") // Foreign key to Account entity
    private Account account;

    @ManyToOne
    @JoinColumn(name = "card_id") // Foreign key to Card entity
    private Card card;
}
