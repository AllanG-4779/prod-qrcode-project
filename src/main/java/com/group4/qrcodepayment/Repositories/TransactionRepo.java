package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, String> {

}
