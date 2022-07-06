package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepo extends JpaRepository<Bank, String> {

    Bank findBankByipslCode(String code);
}
