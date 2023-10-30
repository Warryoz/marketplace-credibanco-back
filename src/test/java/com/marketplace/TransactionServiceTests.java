package com.marketplace;


import com.marketplace.card.Card;
import com.marketplace.card.CardRepository;
import com.marketplace.transaction.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CardRepository cardRepository;

    @Spy
    private ModelMapper modelMapper;
    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void testGetTransactions() {
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();
        when(transactionRepository.findAll()).thenReturn(Collections.singletonList(transaction));
        when(modelMapper.map(transaction, TransactionDto.class)).thenReturn(transactionDto);

        List<TransactionDto> result = transactionService.getTransactions();

        assertThat(result.size()).isEqualTo(1);
        assertThat(transactionDto).isEqualTo(result.get(0));
    }

    @Test
    public void testCreateTransaction() {
        CreateTransactionDto createTransactionDto = new CreateTransactionDto();
        createTransactionDto.setCardHolderName("John");
        createTransactionDto.setCardNumber("0000000000000000");
        createTransactionDto.setCardExpirationDate(LocalDate.now());
        createTransactionDto.setAmount(100.0D);
        createTransactionDto.setTransactionType(TransactionType.PURCHASE);

        Card card = new Card();
        card.setId(1L);
        card.setBalance(100.0);
        card.setNumber("0000000000000000");
        card.setHolderName("John");
        card.setAllowsDollarTransactions(Boolean.TRUE);
        card.setExpirationDate(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(card);
        transaction.setAmount(100.0D);
        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDate(LocalDate.now());

        TransactionDto transactionDto = TransactionDto.builder()
                .transactionStatus(TransactionStatus.SUCCESSFUL)
                .transactionType(TransactionType.PURCHASE)
                .amount(100.0D).build();

        when(cardRepository.findByNumberAndHolderNameAndExpirationDate(
                createTransactionDto.getCardNumber(),
                createTransactionDto.getCardHolderName(),
                createTransactionDto.getCardExpirationDate()
        )).thenReturn(Optional.of(card));
        when(modelMapper.map(createTransactionDto, Transaction.class)).thenReturn(transaction);

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionDto result = transactionService.createTransaction(createTransactionDto);

        assertThat(transactionDto).isEqualTo(result);
    }

    @Test
    public void testCancelTransaction() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(100.0);
        card.setNumber("0000000000000000");
        card.setHolderName("John");
        card.setAllowsDollarTransactions(Boolean.TRUE);
        card.setExpirationDate(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(card);
        transaction.setAmount(100.0D);
        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDate(LocalDate.now());

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        boolean result = transactionService.cancelTransaction(1L);

        assertThat(result).isTrue();
    }


    @Test
    public void testCancelTransactionWhen24HoursPassed() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(100.0);
        card.setNumber("0000000000000000");
        card.setHolderName("John");
        card.setAllowsDollarTransactions(Boolean.TRUE);
        card.setExpirationDate(LocalDate.now());

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCard(card);
        transaction.setAmount(100.0D);
        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        transaction.setDate(LocalDate.now().plusDays(2));

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        boolean result = transactionService.cancelTransaction(1L);

        assertThat(result).isFalse();
    }
}
