package com.cajucard.entities;

import com.cajucard.models.AccountType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private List<String> transferHistory;

    public Account() {
    }

    public Account(Long id, BigDecimal balance, AccountType accountType) {
        this.id = id;
        this.balance = balance;
        this.accountType = accountType;
        this.transferHistory = new ArrayList<>();
    }

    //TODO L4. Questão aberta (safe thread concurrency with synchronized)
    public synchronized void deposit(BigDecimal amount) {
        System.out.println("[Deposit] current balance: " + balance);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            System.out.println("[Deposit] new balance: " + balance);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    //TODO L4. Questão aberta (safe thread concurrency with synchronized)
    public synchronized void withdraw(BigDecimal amount) {
        System.out.println("[Withdraw] current balance: " + balance);
        if (hasBalance(amount)) {
            balance = balance.subtract(amount);
            System.out.println("[Withdraw] new balance: " + balance);
        } else {
            throw new IllegalArgumentException("Insufficient funds or invalid amount");
        }
    }

    //TODO L4. Questão aberta (safe thread concurrency with synchronized)
    public synchronized boolean hasBalance(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) > 0 && balance.compareTo(amount) >= 0;
    }

    //TODO L4. Questão aberta (safe thread concurrency with synchronized)
    public synchronized void addTransferHistory(String transferHistory) {
        this.transferHistory.add(transferHistory);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance.setScale(2, RoundingMode.HALF_EVEN); // Retorna o saldo com duas casas decimais
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public List<String> getTransferHistory() {
        return transferHistory;
    }
}
