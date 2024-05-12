package com.tevind.rocketbank.repositories;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.EndingBalanceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EndingDailyBalanceRepository extends JpaRepository<EndingBalanceSnapshot, Long> {
    Optional<Iterable<EndingBalanceSnapshot>> findByAccount(Account account);

}
