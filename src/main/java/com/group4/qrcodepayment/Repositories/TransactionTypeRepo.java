package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepo extends JpaRepository<TransactionType, Long> {
    Optional<TransactionType> findByTransactionId(Character transactionType);
}
