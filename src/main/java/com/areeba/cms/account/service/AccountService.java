package com.areeba.cms.account.service;
import com.areeba.cms.account.dto.AccountRequestDTO;
import com.areeba.cms.account.dto.AccountResponseDTO;
import com.areeba.cms.account.dto.AccountUpdateDTO;
import com.areeba.cms.account.model.Account;
import com.areeba.cms.accountCard.model.AccountCard;
import com.areeba.cms.card.model.Card;
import com.areeba.cms.enums.Currency;
import com.areeba.cms.enums.Status;
import com.areeba.cms.accountCard.repository.AccountCardRepository;
import com.areeba.cms.account.repository.AccountRepository;
import com.areeba.cms.card.repository.CardRepository;
import com.areeba.cms.transaction.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountCardRepository accountCardRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper mapper;

    public AccountService(AccountRepository accountRepository, AccountCardRepository accountCardRepository, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.mapper = new ModelMapper();
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
        // Fields
        BigDecimal balance = accountRequestDTO.getBalance();
        Currency currency = accountRequestDTO.getCurrency();

        // Create account object
        Account account = new Account(Status.ACTIVE, balance, currency);

        // Persist
        accountRepository.save(account);

        return this.mapper.map(account, AccountResponseDTO.class);
    }

    public AccountResponseDTO getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        return this.mapper.map(account, AccountResponseDTO.class);
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency()))
                .collect(Collectors.toList());
    }

    public AccountResponseDTO update(UUID id, AccountUpdateDTO accountUpdateDTO){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
        account.setStatus(accountUpdateDTO.getStatus());
        account.setBalance(accountUpdateDTO.getBalance());
        accountRepository.save(account);
        return this.mapper.map(account, AccountResponseDTO.class);
    }

    @Transactional
    public void deleteAccount(UUID id) {
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
    }



}
