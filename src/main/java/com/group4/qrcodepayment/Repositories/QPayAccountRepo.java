package com.group4.qrcodepayment.Repositories;

import com.group4.qrcodepayment.models.QPayAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QPayAccountRepo extends JpaRepository<QPayAccount, String> {

}
