package com.areeba.cms.account.service;

import com.areeba.cms.account.model.Account;
import com.areeba.cms.account.records.AccountRequestRecord;
import com.areeba.cms.account.records.AccountResponseRecord;
import com.areeba.cms.account.records.AccountUpdateRecord;
import com.areeba.cms.accountCard.model.AccountCard;
import com.areeba.cms.card.model.Card;
import com.areeba.cms.enums.Status;
import com.areeba.cms.accountCard.repository.AccountCardRepository;
import com.areeba.cms.account.repository.AccountRepository;
import com.areeba.cms.card.repository.CardRepository;
import com.areeba.cms.transaction.repository.TransactionRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCardRepository accountCardRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    public AccountService(AccountRepository accountRepository, AccountCardRepository accountCardRepository, CardRepository cardRepository, TransactionRepository transactionRepository, ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.objectMapper = objectMapper;
    }

    public AccountResponseRecord createAccount(AccountRequestRecord request) {
        // Create account object
        Account account = new Account(Status.ACTIVE, request.balance(), request.currency());
        // Persist
        accountRepository.save(account);
        return objectMapper.convertValue(account, AccountResponseRecord.class);
    }

    public AccountResponseRecord getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
        return objectMapper.convertValue(account, AccountResponseRecord.class);
    }

    public List<AccountResponseRecord> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponseRecord(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency()))
                .collect(Collectors.toList());
    }

    public AccountResponseRecord update(UUID id, AccountUpdateRecord update) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
        account.setStatus(update.status());
        account.setBalance(update.balance());
        accountRepository.save(account);
        return objectMapper.convertValue(account, AccountResponseRecord.class);
    }

    @Transactional
    public String deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        List<AccountCard> accountCards = accountCardRepository.findByAccount(account);
        List<Card> cards = accountCards.stream().map(AccountCard::getCard).toList();

        accountCardRepository.deleteAll(accountCards);

        for (Card card : cards) {
            if (!accountCardRepository.existsByCard(card)) { // If there is no more accountCard rows for a card that exists, this means that the card is now orphaned. meaning, it belonged only to the account we are deleting. Hence, cascade the deletion and delete the card too.
                // Before deleting the card, we need to delete its transactions (since these rows have a foreign key pointing to the card)
                transactionRepository.deleteByCard(card);
                cardRepository.delete(card);
            }
        }

        accountRepository.delete(account);

        return "Accounts and orphaned cards and transactions were deleted.";
    }


}
