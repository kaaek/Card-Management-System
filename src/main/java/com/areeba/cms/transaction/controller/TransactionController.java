package com.areeba.cms.transaction.controller;

import com.areeba.cms.credit.record.CreditRequestRecord;
import com.areeba.cms.debit.record.DebitRequestRecord;
import com.areeba.cms.transaction.record.TransactionRequestRecord;
import com.areeba.cms.transaction.record.TransactionResponseRecord;
import com.areeba.cms.transaction.record.TransactionUpdateRecord;
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
    public ResponseEntity<List<TransactionResponseRecord>> getAllTransactions() {
        List<TransactionResponseRecord> dtos = transactionService.getAllTransactions();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseRecord> getTransactionById(@PathVariable("id") UUID id) {
        TransactionResponseRecord dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<TransactionResponseRecord> createTransaction(@RequestBody TransactionRequestRecord dto) {
        TransactionResponseRecord created = transactionService.createTransaction(dto);
        return ResponseEntity.status(201).body(created);
    }
    
    @PostMapping("/debit")
    public ResponseEntity<TransactionResponseRecord> debit(@RequestBody DebitRequestRecord request) {
        TransactionResponseRecord dto = transactionService.debit(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponseRecord> debit (@RequestBody CreditRequestRecord request) {
        TransactionResponseRecord dto = transactionService.credit(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseRecord> update(@PathVariable("id") UUID id, @RequestBody TransactionUpdateRecord dto) {
        TransactionResponseRecord updated = transactionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable("id") UUID id) {
        String message = transactionService.deleteTransaction(id);
        return ResponseEntity.ok(message);
    }
}