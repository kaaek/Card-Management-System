package com.example.cms.service;

import com.example.cms.dto.AccountRequestDTO;
import com.example.cms.dto.AccountResponseDTO;
import com.example.cms.model.Account;
import com.example.cms.model.enums.Status;
import com.example.cms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO){
        Account account = new Account(Status.ACTIVE, accountRequestDTO.getBalance(), accountRequestDTO.getCurrency());
        accountRepository.save(account);
        return new AccountResponseDTO(account.getId(), account.getStatus(), account.getBalance(), account.getCurrency());
    }


}
