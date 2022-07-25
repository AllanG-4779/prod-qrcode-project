package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.exception.resterrors.BankNotFoundException;
import com.group4.qrcodepayment.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BankRepo extends JpaRepository<Bank, String> {

    Bank findBankByIpslCode(String code);
    @Modifying
    @Transactional
    @Query("UPDATE Bank  bank set bank.supported=true WHERE bank.ipslCode=?1")
    int setSupported(String name) throws BankNotFoundException;
}
