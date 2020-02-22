package com.mindaugas.ledger;

import java.util.Date;
import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

  @Bean
  CommandLineRunner initDatabase(TransactionRepository repository) {
    return args -> {
      repository.save(new Transaction("LT12312", new Date(), "LT1231", "transfer", new BigDecimal(2), "LTL"));
      repository.save(new Transaction("LT12312", new Date(), "LT123213", "transfer", new BigDecimal(3.12), "LTL"));
    };
  }
}
