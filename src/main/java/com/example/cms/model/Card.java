package com.example.cms.model;

import com.example.cms.util.CryptoConverter;
import com.example.cms.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

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
    private Status status;

    @Getter @Setter
    @Column (name = "date", nullable = false)
    private Date expiry;

    @Getter @Setter
    @Convert(converter = CryptoConverter.class)
    @Column(name = "card_number", updatable = false, nullable = false, unique = true)
    private String cardNumber;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccountCard> accounts = new HashSet<>();

    /* DEPRECATED:
    @Getter @Setter
    @ManyToMany(mappedBy = "cards")
    private Set<Account> accounts;
     */

    public Card(){} // TO-DO: include debug messages.

    public Card(Status status, Date expiry, String cardNumber, Set<Account> accounts){
        this.status = status;
        this.expiry = expiry;
        this.cardNumber = cardNumber;
        // this.accounts = accounts;
        // TO-DO: add debug message
    }

    public void activate(){
        setStatus(Status.ACTIVE);
        // System.out.println("Card with ID = "+getId()+" status was set to "+ Status.ACTIVE);
    }

    public void deactivate(){
        setStatus(Status.INACTIVE);
        // System.out.println("Card with ID = "+getId()+" status was set to "+ Status.INACTIVE);
    }

    // public boolean isActive(): return status == Status.ACTIVE
    // public String getMaskedCardNumber(): return masked version of cardNumber (e.g., **** **** **** 1234
    // public String getCardDetails(): formatted string of card info (status, expiry, masked number)
    // (Optional): addAccount(Account account) and removeAccount(Account account) for managing the many-to-many relationship.
}
