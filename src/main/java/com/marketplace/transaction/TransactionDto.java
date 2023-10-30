package com.marketplace.transaction;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    @NotNull
    private Double amount;

    @NotNull
    private Date date;

    private TransactionStatus transactionStatus;

    @NotNull
    private TransactionType transactionType;
}
