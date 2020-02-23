package com.mindaugas.ledger;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
class Balance {
    private String accountNumber;
    private Map<String, BigDecimal> balances;

    Balance() {}

    Balance(String accountNumber, Map<String, BigDecimal> balances) {
        this.accountNumber = accountNumber;
        this.balances = balances;
    }
}