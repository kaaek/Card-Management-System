package com.example.cms.api;

import com.example.cms.dto.account.AccountRequestDTO;
import com.example.cms.dto.account.AccountResponseDTO;
import com.example.cms.dto.account.AccountUpdateDTO;
import com.example.cms.model.enums.Status;
import com.example.cms.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService){this.accountService = accountService;}

    // GET API to fetch all accounts
    @GetMapping("/all")
    public List<AccountResponseDTO> getAllAccounts(){
        return accountService.getAllAccounts();
    }

    // Get API to fetch specific account
    @GetMapping("/{id}")
    public AccountResponseDTO getAccountById(@PathVariable UUID id){
        return accountService.getAccountById(id);
    }

    // POST API to add an account
    @PostMapping("/new")
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO accountRequestDTO){
        return accountService.createAccount(accountRequestDTO);
    }

    // PUT API
    @PutMapping("/{id}")
    public AccountResponseDTO update(@PathVariable UUID id, @RequestBody AccountUpdateDTO accountUpdateDTO){
        return accountService.update(id, accountUpdateDTO);
    }

    // PATCH API
    @PatchMapping("/{id}/balance") // TO-DO: update endpoint
    public AccountResponseDTO updateBalance(@PathVariable UUID id, @RequestBody BigDecimal newBalance){
        return accountService.updateBalance(id, newBalance);
    }

    @PatchMapping("/{id}/status") // TO-DO: update endpoint
    public AccountResponseDTO updateStatus(@PathVariable UUID id, @RequestBody Status newStatus){
        return accountService.updateStatus(id, newStatus);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteAccount (@PathVariable UUID id){
        accountService.deleteAccount(id);
    }

}
