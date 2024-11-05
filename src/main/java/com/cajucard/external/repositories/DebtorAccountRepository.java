package com.cajucard.external.repositories;

import com.cajucard.entities.DebtorAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DebtorAccountRepository extends JpaRepository<DebtorAccount, Long> {

    Optional<DebtorAccount> findByAccountNumber(String accountNumber);
}
