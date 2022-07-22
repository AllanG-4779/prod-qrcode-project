package com.group4.qrcodepayment.externals.mpesa;

import com.group4.qrcodepayment.externals.mpesa.dto.TransactionMetadata;
import org.springframework.stereotype.Service;

@Service
public interface MpesaService {
     void updateTransaction(String transactionId, String status);
     void updateUser(TransactionMetadata transactionMetadata);

}
