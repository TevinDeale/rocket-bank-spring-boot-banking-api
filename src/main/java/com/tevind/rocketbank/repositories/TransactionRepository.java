package com.tevind.rocketbank.repositories;


import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Optional<Page<Transaction>> findAllByAccount(Account account, Pageable pageable);
    Optional<List<Transaction>> findAllByAccount(Account account);
}
