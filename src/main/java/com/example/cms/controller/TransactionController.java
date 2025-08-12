package com.example.cms.controller;

import com.example.cms.dto.transaction.TransactionRequestDTO;
import com.example.cms.dto.transaction.TransactionResponseDTO;
import com.example.cms.dto.transaction.TransactionUpdateDTO;
import com.example.cms.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){this.transactionService = transactionService;}

//    @GetMapping("/all")
//    public List<TransactionResponseDTO> getAllTransactions() {return transactionService.getAllTransactions();}
//
//    @GetMapping("/{id}")
//    public TransactionResponseDTO getTransactionById(@PathVariable UUID id){return transactionService.getTransactionById(id);}
//
//    @PostMapping("/new")
//    public TransactionResponseDTO createTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO){
//        return transactionService.createTransaction(transactionRequestDTO);
//    }
//
//    @PutMapping("/{id}")
//    public TransactionResponseDTO update(@PathVariable UUID id, @RequestBody TransactionUpdateDTO transactionUpdateDTO){
//        return transactionService.update(id, transactionUpdateDTO);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteTransaction (@PathVariable UUID id){transactionService.deleteTransaction(id);}

    @GetMapping("/all")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        List<TransactionResponseDTO> dtos = transactionService.getAllTransactions();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(@PathVariable UUID id) {
        TransactionResponseDTO dto = transactionService.getTransactionById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestBody TransactionRequestDTO dto) {
        TransactionResponseDTO created = transactionService.createTransaction(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(@PathVariable UUID id, @RequestBody TransactionUpdateDTO dto) {
        TransactionResponseDTO updated = transactionService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }


}
