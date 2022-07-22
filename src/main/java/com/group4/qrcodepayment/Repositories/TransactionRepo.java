package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, String> {
@Modifying
    @Transactional
    @Query("UPDATE Transactions transaction SET transaction.status=?2 WHERE transaction.transactionRef=?1")
    void updateTransaction(String transactionId, String status);
}
