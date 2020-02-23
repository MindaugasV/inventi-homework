package com.mindaugas.ledger;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateAfterAndDateBefore(Date fromDate, Date toDate);
    List<Transaction> findByDateAfter(Date fromDate);
    List<Transaction> findByDateBefore(Date toDate);
    
    List<Transaction> findByAccountNumber(String accountNumber);
    List<Transaction> findByAccountNumberAndDateAfterAndDateBefore(String accountNumber, Date fromDate, Date toDate);
    List<Transaction> findByAccountNumberAndDateAfter(String accountNumber, Date fromDate);
    List<Transaction> findByAccountNumberAndDateBefore(String accountNumber, Date toDate);    
}
