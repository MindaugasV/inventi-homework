package com.mindaugas.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class LedgerApplication {
	public static void main(String[] args) {
		SpringApplication.run(LedgerApplication.class, args);
	}

}
