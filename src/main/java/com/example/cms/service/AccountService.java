package com.example.cms.service;
import com.example.cms.dto.account.AccountRequestDTO;
import com.example.cms.dto.account.AccountResponseDTO;
import com.example.cms.dto.account.AccountUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.AccountCard;
import com.example.cms.model.Card;
import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountCardRepository;
import com.example.cms.repository.AccountRepository;
import com.example.cms.repository.CardRepository;
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
    private final ModelMapper mapper;

    public AccountService(AccountRepository accountRepository, AccountCardRepository accountCardRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.accountCardRepository = accountCardRepository;
        this.cardRepository = cardRepository;
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
            if (!accountCardRepository.existsByCard(card)) {
                cardRepository.delete(card);
            }
        }

        accountRepository.delete(account);
    }



}
