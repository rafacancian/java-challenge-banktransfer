package com.cajucard.usecases;

import com.cajucard.entities.Account;
import com.cajucard.entities.DebtorAccount;
import com.cajucard.external.repositories.AccountRepository;
import com.cajucard.external.repositories.DebtorAccountRepository;
import com.cajucard.mock.MockValues;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountService {

    @Autowired
    private DebtorAccountRepository debtorAccountRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Optional<DebtorAccount> findByAccountNumber(String accountNumber) {
        return debtorAccountRepository.findByAccountNumber(accountNumber);
    }

    public void saveTransfer(DebtorAccount debtorAccount) {
        debtorAccountRepository.save(debtorAccount);
    }

    public void createTransferHistory(Account account, BigDecimal amount) {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        StringBuilder transferHistory = new StringBuilder()
                .append("Date: ").append(currentDate)
                .append(" | Amount: ").append(amount)
                .append(" | Balance: ").append(account.getBalance());

        System.out.println(transferHistory);
        account.addTransferHistory(transferHistory.toString());
    }

    @Transactional
    public void saveMockDebtorAccount() {
        Set<Account> accounts = MockValues.createAccounts();
        accountRepository.saveAll(accounts);

        DebtorAccount debtorAccount = MockValues.createDebtorAccount(accounts);
        debtorAccountRepository.save(debtorAccount);

        System.out.println("Mock accounts registered with success to H2 database");
    }
}
