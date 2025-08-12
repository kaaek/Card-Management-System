package com.example.cms.service;
import com.example.cms.dto.account.AccountRequestDTO;
import com.example.cms.dto.account.AccountResponseDTO;
import com.example.cms.dto.account.AccountUpdateDTO;
import com.example.cms.model.Account;
import com.example.cms.model.enums.Currency;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ModelMapper mapper;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.mapper = new ModelMapper();
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
//        Currency currency = parseCurrency(accountRequestDTO.getCurrency());
        // Fields
        BigDecimal balance = accountRequestDTO.getBalance();
        Currency currency = accountRequestDTO.getCurrency();

        // Create account object
        Account account = new Account(Status.ACTIVE, balance, currency);

        // Persist
        accountRepository.save(account);

        return this.mapper.map(account, AccountResponseDTO.class);
//        return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
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
        account.setCurrency(accountUpdateDTO.getCurrency());
        accountRepository.save(account);
        return this.mapper.map(account, AccountResponseDTO.class);
//        return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
    }

    public void deleteAccount(UUID id){
        accountRepository.deleteById(id);
    }


}
