package com.areeba.cms.account.controller;

import com.areeba.cms.account.dto.AccountRequestDTO;
import com.areeba.cms.account.dto.AccountResponseDTO;
import com.areeba.cms.account.dto.AccountUpdateDTO;
import com.areeba.cms.account.records.AccountRequestRecord;
import com.areeba.cms.account.records.AccountResponseRecord;
import com.areeba.cms.account.records.AccountUpdateRecord;
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
    public ResponseEntity<List<AccountResponseRecord>> getAllAccounts(){
        List<AccountResponseRecord> dtos = accountService.getAllAccounts();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseRecord> getAccountById(@PathVariable("id") UUID id){
        AccountResponseRecord dto = accountService.getAccountById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<AccountResponseRecord> createAccount(@RequestBody AccountRequestRecord request){
        AccountResponseRecord dto = accountService.createAccount(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseRecord> update(@PathVariable("id") UUID id, @RequestBody AccountUpdateRecord update){
        AccountResponseRecord dto = accountService.update(id, update);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount (@PathVariable("id") UUID id){
        String msg = accountService.deleteAccount(id);
        return ResponseEntity.ok(msg);
    }

}
