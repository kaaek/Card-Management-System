package com.areeba.cms.account.controller;

import com.areeba.cms.account.dto.AccountRequestDTO;
import com.areeba.cms.account.dto.AccountResponseDTO;
import com.areeba.cms.account.dto.AccountUpdateDTO;
import com.areeba.cms.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService){this.accountService = accountService;}

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(){
        List<AccountResponseDTO> dtos = accountService.getAllAccounts();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(@PathVariable("id") UUID id){
        AccountResponseDTO dto = accountService.getAccountById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody AccountRequestDTO accountRequestDTO){
        AccountResponseDTO dto = accountService.createAccount(accountRequestDTO);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable("id") UUID id, @RequestBody AccountUpdateDTO accountUpdateDTO){
        AccountResponseDTO dto = accountService.update(id, accountUpdateDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount (@PathVariable("id") UUID id){
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
