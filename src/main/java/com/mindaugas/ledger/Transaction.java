package com.mindaugas.ledger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

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

    // CSV Handling

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

    /**
     * CSV line processor for transaction
     */
    public static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), // AccountNumber
                new NotNull(new ParseDate("yyyy-MM-dd HH:mm:ss.SSS")), // Date
                new NotNull(), // Beneficiary
                new Optional(), // Comment
                new Optional(new ParseBigDecimal()), // Amount
                new NotNull() // Currency
        };
        return processors;
    }

    Transaction(Map<String, Object> fieldsMap) {
        // TODO: Fix casting. For now assuming that casting will work.
        this.accountNumber = (String) fieldsMap.get("accountNumber");
        this.date = (Date) fieldsMap.get("date");
        this.beneficiary = (String) fieldsMap.get("beneficiary");
        this.comment = (String) fieldsMap.getOrDefault("comment", "");
        this.amount = (BigDecimal) fieldsMap.get("amount");
        this.currency = (String) fieldsMap.get("currency");
    }
}
