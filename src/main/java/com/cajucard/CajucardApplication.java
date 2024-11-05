package com.cajucard;

import com.cajucard.usecases.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.cajucard.external.repositories")
public class CajucardApplication implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(CajucardApplication.class, args);
    }

    @Override
    public void run(String... args) {
        accountService.saveMockDebtorAccount();
    }
}
