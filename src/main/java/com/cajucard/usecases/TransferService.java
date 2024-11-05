package com.cajucard.usecases;

import com.cajucard.adapters.dtos.CreateTransferRequest;
import com.cajucard.adapters.dtos.CreateTransferResponse;
import com.cajucard.adapters.dtos.TransferResponse;
import com.cajucard.entities.Account;
import com.cajucard.entities.DebtorAccount;
import com.cajucard.mock.MockValues;
import com.cajucard.models.AccountType;
import com.cajucard.models.MCC;
import com.cajucard.models.TransferStatus;
import com.cajucard.usecases.exceptions.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.cajucard.models.AccountType.FOOD;
import static com.cajucard.models.AccountType.MEAL;

@Service
public class TransferService {

    @Autowired
    private AccountService accountService;

    public List<TransferResponse> getTransfer() {
        return MockValues.getAllTransfer();
    }

    public TransferResponse getTransfer(Long id) {
        return MockValues.getTransferById(id);
    }

    public CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest) {
        Optional<DebtorAccount> debtorAccount = accountService.findByAccountNumber(createTransferRequest.account());
        if (debtorAccount.isPresent()) {
            try {
                MCC mcc = MCC.fromValue(createTransferRequest.mcc());
                return handleTransactionByMCC(mcc, createTransferRequest, debtorAccount.get());
            } catch (IllegalArgumentException e) {
                //TODO L3.Dependente do comerciante
                return handleTransactionByMerchantName(createTransferRequest, debtorAccount.get());
            }
        }
        throw new AccountNotFoundException();

    }

    private CreateTransferResponse handleTransactionByMerchantName(CreateTransferRequest createTransferRequest, DebtorAccount debtorAccount) {
        AccountType accountType = getAccountTypeByMerchantName(createTransferRequest.merchant());
        return switch (accountType) {
            case FOOD -> processInitTransfer(FOOD, debtorAccount, createTransferRequest.totalAmount());
            case MEAL -> processInitTransfer(MEAL, debtorAccount, createTransferRequest.totalAmount());
            default -> processInitTransfer(AccountType.CASH, debtorAccount, createTransferRequest.totalAmount());
        };
    }

    private CreateTransferResponse handleTransactionByMCC(MCC mcc, CreateTransferRequest createTransferRequest, DebtorAccount debtorAccount) {
        return switch (mcc) {
            case MCC_5411, MCC_5412 -> processInitTransfer(FOOD, debtorAccount, createTransferRequest.totalAmount());
            case MCC_5811, MCC_5812 -> processInitTransfer(MEAL, debtorAccount, createTransferRequest.totalAmount());
            default -> processInitTransfer(AccountType.CASH, debtorAccount, createTransferRequest.totalAmount());
        };
    }

    private AccountType getAccountTypeByMerchantName(String merchant) {
        if (merchant.contains(FOOD.name())) {
            return FOOD;
        } else if (merchant.contains(MEAL.name())) {
            return MEAL;
        }
        return AccountType.CASH;
    }

    private CreateTransferResponse processInitTransfer(AccountType accountType, DebtorAccount debtorAccount, BigDecimal totalAmount) {
        //TODO L1. Autorizador simples
        return debtorAccount.getAccounts().stream()
                .filter(account -> account.getAccountType() == accountType)
                .findFirst()
                .map(account -> {
                    if (account.hasBalance(totalAmount)) {
                        return processTransferSuccess(debtorAccount, totalAmount, account);
                    } else {
                        // TODO L2. Autorizador com fallback
                        debtorAccount.getAccounts().stream()
                                .filter(account1 -> accountType == AccountType.CASH && account1.hasBalance(totalAmount))
                                .findFirst()
                                .map(account1 ->
                                        processTransferSuccess(debtorAccount, totalAmount, account))
                                .orElseThrow(AccountNotFoundException::new);
                    }
                    return new CreateTransferResponse(TransferStatus.REJECTED.getStatus());
                })
                .orElseThrow(AccountNotFoundException::new);
    }

    private CreateTransferResponse processTransferSuccess(DebtorAccount debtorAccount, BigDecimal totalAmount, Account account) {
        account.withdraw(totalAmount);
        accountService.createTransferHistory(account, totalAmount);
        accountService.saveTransfer(debtorAccount);
        return new CreateTransferResponse(TransferStatus.APPROVED.getStatus());
    }
}