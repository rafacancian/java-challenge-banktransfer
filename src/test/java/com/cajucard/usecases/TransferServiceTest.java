package com.cajucard.usecases;

import com.cajucard.adapters.dtos.CreateTransferRequest;
import com.cajucard.adapters.dtos.CreateTransferResponse;
import com.cajucard.entities.Account;
import com.cajucard.entities.DebtorAccount;
import com.cajucard.models.AccountType;
import com.cajucard.models.MCC;
import com.cajucard.models.TransferStatus;
import com.cajucard.usecases.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TransferServiceTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @EnumSource(value = MCC.class, names = {"MCC_5411", "MCC_5412", "MCC_5811", "MCC_5812"})
    void whenCallCreateTransferWithMCC_thenProcessTransferWithSuccess(MCC mcc) {
        CreateTransferRequest request = getCreateTransferRequest(mcc);
        DebtorAccount debtorAccount = getDebtorAccount();

        when(accountService.findByAccountNumber(any())).thenReturn(Optional.of(debtorAccount));
        CreateTransferResponse response = transferService.createTransfer(request);

        assertEquals(TransferStatus.APPROVED.getStatus(), response.code());
    }

    @Test
    void whenCallCreateTransferWithMCCWrong_thenProcessTransferWithSuccessByMerchantName() {
        CreateTransferRequest request = getCreateTransferRequest("MCC-XX01");
        DebtorAccount debtorAccount = getDebtorAccount();

        when(accountService.findByAccountNumber(any())).thenReturn(Optional.of(debtorAccount));
        CreateTransferResponse response = transferService.createTransfer(request);

        assertEquals(TransferStatus.APPROVED.getStatus(), response.code());
    }

    //TODO is missing unit tests for other cases

    @Test
    void testCreateTransfer_AccountNotFound() {
        when(accountService.findByAccountNumber(any())).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> transferService.createTransfer(getCreateTransferRequest(MCC.MCC_5411)));
    }

    private static DebtorAccount getDebtorAccount() {
        return new DebtorAccount(1L, "123", "Rafael", getAccounts());
    }

    private static CreateTransferRequest getCreateTransferRequest(MCC mcc) {
        return new CreateTransferRequest("1", BigDecimal.valueOf(1000.00), mcc.getValue(), "Merchant name");
    }

    private static CreateTransferRequest getCreateTransferRequest(String mcc) {
        return new CreateTransferRequest("1", BigDecimal.valueOf(1000.00), mcc, "Merchant name");
    }

    private static Set<Account> getAccounts() {
        Account accountFood = new Account(1L, BigDecimal.valueOf(2000), AccountType.FOOD);
        Account accountMeal = new Account(2L, BigDecimal.valueOf(3000), AccountType.MEAL);
        Account accountCash = new Account(3L, BigDecimal.valueOf(4000), AccountType.CASH);
        return Set.of(accountFood, accountMeal, accountCash);
    }
}
