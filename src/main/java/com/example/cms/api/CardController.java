package com.example.cms.api;

import com.example.cms.dto.card.CardRequestDTO;
import com.example.cms.dto.card.CardResponseDTO;
import com.example.cms.dto.card.CardUpdateDTO;
import com.example.cms.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService){
        this.cardService = cardService;
    }

    // GET APIs
    @GetMapping("/all")
    public List<CardResponseDTO> getAllCards(){return cardService.getAllCards();}

    @GetMapping("/{id}")
    public CardResponseDTO getCardById(@PathVariable UUID id){return cardService.getCardById(id);}

    // POST API
    /* DEPRECATED:
    @PostMapping("/new")
    public CardResponseDTO createCard(@RequestParam String accountIds) {
        Set<UUID> ids = Arrays.stream(accountIds.split(","))
                .map(String::trim)
                .map(UUID::fromString)
                .collect(Collectors.toSet());
        CardRequestDTO cardRequestDTO = new CardRequestDTO();
        cardRequestDTO.setAccountIds(ids);
        return cardService.createCard(cardRequestDTO);
    }
    */
    @PostMapping("/new")
    public CardResponseDTO createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        return cardService.createCard(cardRequestDTO);
    }

    // PUT API
    @PutMapping("/{id}")
    public CardResponseDTO update(@PathVariable UUID id, @RequestBody CardUpdateDTO cardUpdateDTO){
        return cardService.update(id, cardUpdateDTO);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteCard (@PathVariable UUID id) {cardService.deleteCard(id);}

}
