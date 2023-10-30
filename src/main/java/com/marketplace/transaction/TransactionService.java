package com.marketplace.transaction;


import com.marketplace.card.Card;
import com.marketplace.card.CardRepository;
import com.marketplace.exceptions.ResourceNotFoundException;
import com.marketplace.utils.Constants;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    private final ModelMapper modelMapper;

    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll()
                .stream().map(
                        (tran) -> modelMapper.map(tran, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDto createTransaction(CreateTransactionDto createTransactionDto) {
        Card card = cardRepository.findByNumberAndHolderNameAndExpirationDate(
                        createTransactionDto.getCardNumber(),
                        createTransactionDto.getCardHolderName(),
                        createTransactionDto.getCardExpirationDate())
                .orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        Transaction transaction = modelMapper.map(createTransactionDto, Transaction.class);

        double transactionAmount = createTransactionDto.getAmount();
        double currentBalance = card.getBalance();
        card.setBalance(currentBalance - transactionAmount);

        transaction.setCard(card);
        transaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        Transaction newTransaction = transactionRepository.save(transaction);

        return modelMapper.map(newTransaction, TransactionDto.class);
    }

    @Transactional
    public boolean cancelTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + transactionId));

        Card card = transaction.getCard();

        // Check if the transaction is within 24 hours
        LocalDate transactionTime = transaction.getDate();

        LocalDateTime now = LocalDateTime.now(); // Current time
        LocalDateTime transactionDateTime = transactionTime.atStartOfDay();
        long timeDifferenceInMillis = Duration.between(now, transactionDateTime).toMillis();

        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);

        if (hoursDifference <= Constants.MAX_EXPIRATION_DATE_IN_HOURS) {
            transaction.setTransactionStatus(TransactionStatus.CANCELLED);
            double transactionAmount = transaction.getAmount();
            double currentBalance = card.getBalance();
            card.setBalance(currentBalance + transactionAmount);

            cardRepository.save(card);
            transactionRepository.save(transaction);

            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

}
