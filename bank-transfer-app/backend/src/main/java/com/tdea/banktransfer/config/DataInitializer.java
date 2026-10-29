package com.tdea.banktransfer.config;

import com.tdea.banktransfer.entity.Account;
import com.tdea.banktransfer.repository.AccountRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Loads demo accounts the first time the application starts on an empty database. */
@Configuration
public class DataInitializer {

    /**
     * Seeds three demo accounts when the table is empty.
     *
     * @param repository account repository
     * @return the runner executed at startup
     */
    @Bean
    CommandLineRunner seedAccounts(AccountRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.save(new Account("1000000001", "Diego Restrepo", new BigDecimal("5000000.00")));
            repository.save(new Account("1000000002", "Pedro Gomez", new BigDecimal("2500000.00")));
            repository.save(new Account("1000000003", "Laura Mejia", new BigDecimal("800000.00")));
        };
    }
}
