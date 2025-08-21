package com.areeba.cms.card.service;

import com.areeba.cms.account.model.Account;
import com.areeba.cms.accountCard.model.AccountCard;
import com.areeba.cms.card.model.Card;
import com.areeba.cms.card.record.CardRequestRecord;
import com.areeba.cms.card.record.CardResponseRecord;
import com.areeba.cms.card.record.CardUpdateRecord;
import com.areeba.cms.enums.Status;
import com.areeba.cms.accountCard.repository.AccountCardRepository;
import com.areeba.cms.account.repository.AccountRepository;
import com.areeba.cms.card.repository.CardRepository;
import com.areeba.cms.transaction.repository.TransactionRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final AccountCardRepository accountCardRepository;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper mapper;
//    private final ModelMapper mapper;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, AccountCardRepository accountCardRepository, TransactionRepository transactionRepository, ObjectMapper mapper) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    public CardResponseRecord createCard(CardRequestRecord request) {

        // Fetch accounts
        List<Account> accounts = accountRepository.findAllById(request.accountIds());
        if (accounts.size() != request.accountIds().size()) {
            throw new EntityNotFoundException("One or more Account IDs are invalid");
        }

        // Create card
        Card newCard = new Card(Status.ACTIVE, newExpiryDate(), generate16DigitNumber());

        cardRepository.save(newCard);

        // Link to accounts via join table
        for (Account account : accounts) {
            System.out.println("hello");
            AccountCard link = new AccountCard(account, newCard);
            accountCardRepository.save(link);
        }
        return mapper.convertValue(newCard, CardResponseRecord.class);
    }

    public CardResponseRecord getCardById(UUID id){

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));

        return mapper.convertValue(card, CardResponseRecord.class);
    }

    public List<CardResponseRecord> getAllCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> new CardResponseRecord(card.getId(), card.getStatus(), card.getExpiry(), card.getCardNumber()))
                .collect(Collectors.toList());
    }

    public CardResponseRecord update(UUID id, CardUpdateRecord update){

        // Extract card if exists
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: "+ id));

        card.setStatus(update.status());
        card.setExpiry(update.expiry());

        cardRepository.save(card);

        return mapper.convertValue(card, CardResponseRecord.class);
    }

    public void deleteCard(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + id));

        // the associated transaction rows have a foreign key pointing to the card object, so we need to delete them first.
        transactionRepository.deleteByCard(card);

        List<AccountCard> accountCards = accountCardRepository.findByCard(card);
        accountCardRepository.deleteAll(accountCards);

        cardRepository.delete(card);
    }


    public String generate16DigitNumber() {
        Random random = new Random();
        String cardNumber;

        do {
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                int digit = random.nextInt(10); // 0-9
                sb.append(digit);
            }
            cardNumber = sb.toString(); // TO-DO: check if this number is an existing cardNumber in the db (via card repo)
        } while (cardRepository.existsByCardNumber(cardNumber));

        return cardNumber;
    }

    public Date newExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 3);
        return cal.getTime();
    }
}