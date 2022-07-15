package com.group4.qrcodepayment.service;

import com.group4.qrcodepayment.Repositories.*;
import com.group4.qrcodepayment.dto.TransactionDto;
import com.group4.qrcodepayment.exception.resterrors.TransactionNotFoundException;
import com.group4.qrcodepayment.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionServiceImpl implements Transactionservice {
//    transaction types database
    @Autowired
    private TransactionsTypes transactionTypes;
    @Autowired
    private UserRegistrationImpl userRegistration;

    @Autowired
    private QPayAccountService qPayAccountService;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private BankRepo bankRepo;
//    This method saves a transaction to a database.
//    Receives a transaction dto that contains all the request that are required to save a transaction
    @Override
    public void addTransaction(TransactionDto transactionDto) throws TransactionNotFoundException {
//         get the transaction type
        Optional<TransactionType> transactionType = transactionTypes.findById("D");
//        Given the user's account number and the bank ID can you fetch the user's account
//        Bank bank = bankRepo.findBankByipslCode("11");
//        UserInfo user = userRegistration
//                .findUserByAccountAndBank
//                        (transactionDto.getSourceAccount(), bank);

        if (!transactionType.isPresent()){
            throw new TransactionNotFoundException("Bad transaction passed");
        }
        Transactions transactions = Transactions.builder()
                .amount(transactionDto.getTransactionAmount())
                .transactionRef(transactionDto.getTransactionRef())
                .dateTime(transactionDto.getDate())
                .transactionType(transactionType.get())
                .destinationAccount(transactionDto.getDestinationAccount())
                .sourceAccount(transactionDto.getSourceAccount())
                .build();

//        Now build the QPayDto-> Responsible for updating the QPay account
//        It notes the user and the amount the user has deposited in his account
//        QpayAccountDto dto = QpayAccountDto.builder()
//                .userInfo(user)
//                .amount(Integer.valueOf(transactionDto.getTransactionAmount()))
//                .build();
//        update the transaction first in the user account database.
      //  qPayAccountService.updateAccount(dto);
//        Now add the transaction to the list of transactions in the database.

         transactionRepo.save(transactions);

    }

    @Override
    public Transactions getTransactionById(String id) {
       Optional<Transactions> transaction =  transactionRepo.findById(id);
        return transaction.orElse(null);
    }
}
