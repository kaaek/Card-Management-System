package com.example.cms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cards")
public class Card {
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
    @Column (name = "date", nullable = false)
    private Date expiry;

    @Getter @Setter
    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", updatable = false, nullable = false, unique = true)
    private String cardNumber;

    @Getter @Setter
    @ManyToMany(mappedBy = "cards")
    private Set<Account> accounts;

    // No arg constructor JPA uses when retrieving info from the db
    public Card(){
        this.accounts = new HashSet<>();
    }

    // Parameterized constructor:
    public Card(CardStatus status, Date expiry, String cardNumber){
        this.status = status;
        this.expiry = expiry;
        this.cardNumber = cardNumber;
    }

}
