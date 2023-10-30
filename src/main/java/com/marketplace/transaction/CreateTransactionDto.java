package com.marketplace.transaction;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CreateTransactionDto extends TransactionDto{

    @NotEmpty
    @Size(min = 16, max = 16, message = "Card number should have 16 characters")
    private String cardNumber;

    @NotEmpty
    private String cardHolderName;

    @NotNull
    private LocalDate cardExpirationDate;

}
