package com.mindaugas.ledger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
class Transaction {
    // TODO: add mandatory, optional annotations
    // TODO: add field validations
    private @Id @GeneratedValue Long id; 
    private String accountNumber; 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date date;
    private String beneficiary; 
    private String comment; 
    private BigDecimal amount;
    private String currency;

    Transaction() {}

    Transaction(String accountNumber, Date date, String beneficiary, String comment, BigDecimal amount, String currency) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }

    public static String[] csvHeaderAnnotation() {
        String[] headers = { "accountNumber", "date", "beneficiary", "comment", "amount", "currency" };
        return headers;
    }

    public List<String> csvParams() {
        List<String> list = new ArrayList<String>();

        list.add(accountNumber.toString());
        list.add(date.toString());
        list.add(beneficiary.toString());
        list.add(comment.toString());
        list.add(amount.toString());
        list.add(currency.toString());

        return list;
    }
}