package com.example.cms.controller;

import com.example.cms.dto.card.CardRequestDTO;
import com.example.cms.dto.card.CardResponseDTO;
import com.example.cms.dto.card.CardUpdateDTO;
import com.example.cms.service.CardService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/all")
    public List<CardResponseDTO> getAllCards(){return cardService.getAllCards();}

    @GetMapping("/{id}")
    public CardResponseDTO getCardById(@PathVariable UUID id){return cardService.getCardById(id);}

    @PostMapping("/new")
    public CardResponseDTO createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        return cardService.createCard(cardRequestDTO);
    }

    @PutMapping("/{id}")
    public CardResponseDTO update(@PathVariable UUID id, @RequestBody CardUpdateDTO cardUpdateDTO){
        return cardService.update(id, cardUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCard (@PathVariable UUID id) {cardService.deleteCard(id);}

}
