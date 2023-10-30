package com.marketplace.card;

import com.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.utils.Constants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    private final ModelMapper modelMapper;

    public CardDto createCard(CardDto cardDto) {
        var creationDate = LocalDate.now();

        cardDto.setExpirationDate(creationDate.plusYears(Constants.EXPIRATION_DATA_YEARS));
        Card card = modelMapper.map(cardDto, Card.class);
        Card newCard = cardRepository.save(card);

        return modelMapper.map(newCard, CardDto.class);
    }

    public CardDto reloadCard(Long cardId, double amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id" + cardId));

        double currentBalance = card.getBalance();
        double newBalance = currentBalance + amount;
        card.setBalance(newBalance);
        Card reloadedCard = cardRepository.save(card);
        return modelMapper.map(reloadedCard, CardDto.class);
    }
}
