package com.areeba.cms.card.controller;

import com.areeba.cms.card.dto.CardRequestDTO;
import com.areeba.cms.card.dto.CardResponseDTO;
import com.areeba.cms.card.dto.CardUpdateDTO;
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
    public ResponseEntity<List<CardResponseDTO>> getAllCards(){
        List<CardResponseDTO> dtos = cardService.getAllCards();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable("id") UUID id){
        CardResponseDTO dto = cardService.getCardById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/new")
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        CardResponseDTO dto = cardService.createCard(cardRequestDTO);
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDTO> update (@PathVariable("id") UUID id, @RequestBody CardUpdateDTO cardUpdateDTO) {
        CardResponseDTO dto = cardService.update(id, cardUpdateDTO);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard (@PathVariable("id") UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}