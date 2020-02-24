package com.mindaugas.ledger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.*;

class BalanceCalculator {

    public static Balance balanceFromTransactions(List<Transaction> transactions, String accountNumber) {
        Map<String, BigDecimal> map = transactions.stream().collect(
            Collectors.groupingBy(Transaction::getCurrency,
            Collectors.mapping(Transaction::getAmount, 
            Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)))); 

        Balance balance = new Balance(accountNumber, map);
        
        return balance;
    } 
}

