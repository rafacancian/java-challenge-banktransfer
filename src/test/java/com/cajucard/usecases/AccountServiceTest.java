package com.cajucard.usecases;

import com.cajucard.entities.Account;
import com.cajucard.entities.DebtorAccount;
import com.cajucard.external.repositories.AccountRepository;
import com.cajucard.external.repositories.DebtorAccountRepository;
import com.cajucard.models.AccountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private DebtorAccountRepository debtorAccountRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCallFindByAccountNumberWithAValidAccountNumber_thenReturnsDebtorAccount() {
        when(debtorAccountRepository.findByAccountNumber(any())).thenReturn(Optional.empty());

        Optional<DebtorAccount> result = accountService.findByAccountNumber("123");

        Assertions.assertTrue(result.isEmpty());
        verify(debtorAccountRepository, times(1)).findByAccountNumber("123");
    }

    @Test
    void whenCallFindByAccountNumberWithAInvalidAccountNumber_thenReturnsDebtorAccount() {
        when(debtorAccountRepository.findByAccountNumber(any())).thenReturn(null);

        Optional<DebtorAccount> result = accountService.findByAccountNumber("123");

        assertNull(result);
        verify(debtorAccountRepository, times(1)).findByAccountNumber("123");
    }

    @Test
    void whenCallSaveTransfer_thenSaveTransferWithSuccess() {
        DebtorAccount debtorAccount = getDebtorAccount("12345");
        accountService.saveTransfer(debtorAccount);
        verify(debtorAccountRepository, times(1)).save(debtorAccount);
    }

    @Test
    void testCallCreateTransferHistory_thenCreateTransferHistoryRegister() {
        Account account = getAccount(BigDecimal.valueOf(1000.00), AccountType.FOOD);

        accountService.createTransferHistory(account, BigDecimal.valueOf(200.00));

        assertEquals(1, account.getTransferHistory().size());
        assertTrue(account.getTransferHistory().get(0).contains("Amount: 200.0"));
    }

    private static DebtorAccount getDebtorAccount(String accountNumber) {
        return new DebtorAccount(1L, accountNumber, "Rafael", null);
    }

    private static Account getAccount(BigDecimal amount, AccountType accountType) {
        return new Account(1L, amount, accountType);
    }
}
