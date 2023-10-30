package com.marketplace.card;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CardDto {

    @NotEmpty
    @Size(min = 16, max = 16, message = "Card number should have 16 characters")
    private String number;

    @NotEmpty
    private String holderName;

    @NotNull
    private Double balance = 0D;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    private boolean allowsDollarTransactions;

    @NotNull
    private CardType cardType;
}
