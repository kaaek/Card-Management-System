package com.example.cms.api;

import com.example.cms.dto.transaction.TransactionRequestDTO;
import com.example.cms.dto.transaction.TransactionResponseDTO;
import com.example.cms.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){this.transactionService = transactionService;}

    @GetMapping("/all")
    public List<TransactionResponseDTO> getAllTransactions() {return transactionService.getAllTransactions();}

    @GetMapping("/{id}")
    public TransactionResponseDTO getTransactionById(@PathVariable UUID id){return transactionService.getTransactionById(id);}

    @PostMapping("/new")
    public TransactionResponseDTO createTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){
        return transactionService.createTransation(transactionRequestDTO);
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO update(@PathVariable UUID id, @RequestBody TransactionUpdateDTO transactionUpdateDTO){
        return transactionService.update(id, transactionUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction (@PathVariable UUID id){transactionService.deleteTransaction(id);}

}
