package com.example.cms.service;

import com.example.cms.dto.AccountRequestDTO;
import com.example.cms.dto.AccountResponseDTO;
import com.example.cms.dto.AccountUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
        Currency currency;
        try {
            currency = Currency.valueOf(accountRequestDTO.getCurrency().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency. Supported values: USD, LBP.");
        }
        Account account = new Account(Status.ACTIVE, accountRequestDTO.getBalance(), currency);
        accountRepository.save(account);
        return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
    }

    public AccountResponseDTO getAccountById(UUID id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
        } else {
            throw new RuntimeException("Account not found"); // TO-DO: replace with custom exception if desired
        }
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency()))
                .collect(Collectors.toList());
    }

    public AccountResponseDTO update(UUID id, AccountUpdateDTO accountUpdateDTO){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setStatus(accountUpdateDTO.getStatus());
            account.setBalance(accountUpdateDTO.getBalance());
            return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
        } else {
            throw new RuntimeException("Account not found"); // TO-DO: replace with custom exception if desired
        }
    }

    public AccountResponseDTO updateBalance(UUID id, BigDecimal newBalance){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setBalance(newBalance);
            return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
        } else {
            throw new RuntimeException("Account not found"); // TO-DO: replace with custom exception if desired
        }
    }

    public AccountResponseDTO updateStatus(UUID id, Status newStatus){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setStatus(newStatus);
            return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
        } else {
            throw new RuntimeException("Account not found"); // TO-DO: replace with custom exception if desired
        }
    }

    public void deleteAccount(UUID id){
        accountRepository.deleteById(id);
    }


}
