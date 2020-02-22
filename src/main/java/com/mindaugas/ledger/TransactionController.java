package com.mindaugas.ledger;

import java.util.List;

import org.springframework.web.bind.annotation.*;

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
}
