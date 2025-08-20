package com.areeba.cms.transaction.controller;

import com.areeba.cms.credit.dto.CreditRequestDTO;
import com.areeba.cms.debit.dto.DebitRequestDTO;
import com.areeba.cms.transaction.dto.TransactionRequestDTO;
import com.areeba.cms.transaction.dto.TransactionResponseDTO;
import com.areeba.cms.transaction.dto.TransactionUpdateDTO;
import com.areeba.cms.transaction.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){this.transactionService = transactionService;}

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> dtos = transactionService.getAllTransactions();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable("id") UUID id) {
        TransactionResponseDTO dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO created = transactionService.createTransaction(dto);
        return ResponseEntity.status(201).body(created);
    }
    
    @PostMapping("/debit")
    public ResponseEntity<TransactionResponseDTO> debit(@RequestBody DebitRequestDTO request) {
        TransactionResponseDTO dto = transactionService.debit(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponseDTO> debit (@RequestBody CreditRequestDTO request) {
        TransactionResponseDTO dto = transactionService.credit(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable("id") UUID id, @RequestBody TransactionUpdateDTO dto) {
        TransactionResponseDTO updated = transactionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}