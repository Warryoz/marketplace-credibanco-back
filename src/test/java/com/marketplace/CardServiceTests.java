package com.marketplace;


import com.marketplace.card.Card;
import com.marketplace.card.CardDto;
import com.marketplace.card.CardRepository;
import com.marketplace.card.CardService;
import com.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.transaction.TransactionRepository;
import com.marketplace.transaction.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTests {

    @Mock
    private CardRepository cardRepository;

    @Spy
    private ModelMapper modelMapper;
    @InjectMocks
    private CardService cardService;


    @Test
    public void testCreateCard() {
        CardDto cardDto = new CardDto();
        cardDto.setBalance(100.0);
        cardDto.setNumber("0000000000000000");
        cardDto.setHolderName("John");
        cardDto.setAllowsDollarTransactions(Boolean.TRUE);
        cardDto.setExpirationDate(LocalDate.now().plusYears(3));

        Card card = new Card();
        card.setId(1L);
        card.setBalance(100.0);
        card.setNumber("0000000000000000");
        card.setHolderName("John");
        card.setAllowsDollarTransactions(Boolean.TRUE);
        card.setExpirationDate(LocalDate.now().plusYears(3));

        when(modelMapper.map(cardDto, Card.class)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        CardDto result = cardService.createCard(cardDto);

        assertThat(cardDto).isEqualTo(result);
    }

    @Test
    public void testReloadCard() {
        Long cardId = 1L;
        double amount = 100.0;

        Card card = new Card();
        double initialBalance = 200.0;
        card.setBalance(initialBalance);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        CardDto result = cardService.reloadCard(cardId, amount);

        assertThat(initialBalance + amount).isEqualTo(result.getBalance());
    }

    @Test
    public void testReloadCardCardNotFound() {
        Long cardId = 1L;
        double amount = 100.0;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardService.reloadCard(cardId, amount));
    }

}
