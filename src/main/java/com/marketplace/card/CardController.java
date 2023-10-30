package com.marketplace.card;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@AllArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<CardDto> createCard(@Valid @RequestBody CardDto cardDto) {
        return new ResponseEntity<>(cardService.createCard(cardDto), HttpStatus.CREATED);
    }

    @PostMapping("/reload/{cardId}")
    public ResponseEntity<CardDto> reloadCard(@PathVariable Long cardId, @RequestParam double amount) {
        return new ResponseEntity<>(cardService.reloadCard(cardId, amount), HttpStatus.OK);
    }
}

