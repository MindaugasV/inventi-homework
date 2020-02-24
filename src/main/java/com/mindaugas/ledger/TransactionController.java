package com.mindaugas.ledger;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.NestedServletException;

@RestController
class TransactionController {

    private final TransactionRepository repository;

    TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transactions")
    List<Transaction> all() {
        return repository.findAll();
    }

    @PostMapping("/transactions")
    Transaction newTransaction(@RequestBody Transaction newTransaction) {
        return repository.save(newTransaction);
    }

    @GetMapping("/transactions/export")
    public void exportTransactions(HttpServletResponse response,
        @RequestParam (name="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
        @RequestParam (name="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate
    ) throws IOException {

        List<Transaction> listOfTransactions;
        if (fromDate != null && toDate != null) {
            listOfTransactions = repository.findByDateAfterAndDateBefore(fromDate, toDate);
        } else if (fromDate != null) {
            listOfTransactions = repository.findByDateAfter(fromDate);
        } else if (toDate != null) {
            listOfTransactions = repository.findByDateBefore(toDate);
        } else {
            listOfTransactions = repository.findAll();
        }

        // Configure Response
        response.setContentType("text/csv");
        String csvFileName = "report.csv"; 
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
        CSVParser.writeTransactionsToResponseAsCSVFile(response, listOfTransactions);
    }
    
    @PostMapping("/transactions/import")
    public void importTransactions(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, NestedServletException {
        CSVParser.parseTransactionsCSVFile(file, repository);        
        redirectAttributes.addFlashAttribute("message", 
                "You successfully uploaded " + file.getOriginalFilename() + "!");
    }

    @GetMapping("/account/{accountNumber}/balance")
    Balance balance(@PathVariable String accountNumber,
        @RequestParam (name="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
        @RequestParam (name="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate
    ) {
        List<Transaction> transactions;
        if (fromDate != null && toDate != null) {
            transactions = repository.findByAccountNumberAndDateAfterAndDateBefore(accountNumber, fromDate, toDate);
        } else if (fromDate != null) {
            transactions = repository.findByAccountNumberAndDateAfter(accountNumber, fromDate);
        } else if (toDate != null) {
            transactions = repository.findByAccountNumberAndDateBefore(accountNumber, toDate);
        } else {
            transactions = repository.findByAccountNumber(accountNumber);
        }
        
        Balance balance = BalanceCalculator.balanceFromTransactions(transactions, accountNumber);
        return balance;
    }
}
