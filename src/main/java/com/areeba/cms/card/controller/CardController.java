package com.areeba.cms.card.controller;

import com.areeba.cms.card.record.CardRequestRecord;
import com.areeba.cms.card.record.CardResponseRecord;
import com.areeba.cms.card.record.CardUpdateRecord;
import com.areeba.cms.card.service.CardService;
import com.areeba.cms.transaction.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService, TransactionService transactionService) {
        this.cardService = cardService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CardResponseRecord>> getAllCards(){
        List<CardResponseRecord> dtos = cardService.getAllCards();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseRecord> getCardById(@PathVariable("id") UUID id){
        CardResponseRecord dto = cardService.getCardById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<CardResponseRecord> createCard(@RequestBody CardRequestRecord request) {
        CardResponseRecord dto = cardService.createCard(request);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseRecord> update (@PathVariable("id") UUID id, @RequestBody CardUpdateRecord cardUpdateRecord) {
        CardResponseRecord dto = cardService.update(id, cardUpdateRecord);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard (@PathVariable("id") UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}