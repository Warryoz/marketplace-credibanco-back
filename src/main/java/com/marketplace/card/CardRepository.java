package com.marketplace.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByNumberAndHolderNameAndExpirationDate(String number, String holderName, LocalDate expirationDate);
}
