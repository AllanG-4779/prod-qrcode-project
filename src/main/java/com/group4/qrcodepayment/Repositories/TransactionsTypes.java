package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsTypes extends JpaRepository<TransactionType, String> {
}
