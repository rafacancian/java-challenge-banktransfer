package com.cajucard.entities;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class DebtorAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String accountHolderName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "debtor_account_id")
    private Set<Account> accounts;

    public DebtorAccount() {
    }

    public DebtorAccount(Long id, String accountNumber, String accountHolderName, Set<Account> accounts) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accounts = accounts;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
}
