package com.cajucard.mock;

import com.cajucard.adapters.dtos.TransferResponse;
import com.cajucard.entities.Account;
import com.cajucard.entities.DebtorAccount;
import com.cajucard.models.AccountType;
import com.cajucard.models.MCC;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class MockValues {

    public static List<TransferResponse> getAllTransfer() {
        // findAll and return. Mocking value
        return List.of(new TransferResponse(1L, "123", "100.00", MCC.MCC_5411.getValue(),
                "PADARIA DO ZE - SAO PAULO BR"));
    }


    public static TransferResponse getTransferById(Long id) {
        // findById and return. Mocking value
        return new TransferResponse(id, "123", "100.00", MCC.MCC_5411.getValue(),
                "PADARIA DO ZE - SAO PAULO BR");
    }

    public static Set<Account> createAccounts() {
        Account accountFood = new Account(1L, new BigDecimal("2000"), AccountType.FOOD);
        Account accountMeal = new Account(2L, new BigDecimal("3000"), AccountType.MEAL);
        Account accountCash = new Account(3L, new BigDecimal("4000"), AccountType.CASH);
        return Set.of(accountFood, accountMeal, accountCash);
    }

    public static DebtorAccount createDebtorAccount(Set<Account> accounts) {
        return new DebtorAccount(
                1L, "123", "Rafael A Cancian",
                accounts
        );
    }
}
