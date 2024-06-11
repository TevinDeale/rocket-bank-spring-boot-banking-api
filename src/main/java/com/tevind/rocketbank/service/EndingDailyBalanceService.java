package com.tevind.rocketbank.service;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.EndingBalanceSnapshot;
import com.tevind.rocketbank.repositories.EndingDailyBalanceRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@EnableScheduling
public class EndingDailyBalanceService {

    private final Logger logger = LoggerFactory.getLogger(EndingDailyBalanceService.class);
    private final EndingDailyBalanceRepository endingDailyBalanceRepository;
    private final AccountService accountService;

    public EndingDailyBalanceService(EndingDailyBalanceRepository endingDailyBalanceRepository, AccountService accountService) {
        this.endingDailyBalanceRepository = endingDailyBalanceRepository;
        this.accountService = accountService;
    }

    @Transactional
    @Scheduled(cron = "0 59 23 * * *")
    public void endingDailyBalanceCronJob() {
        logger.info("endingDailyBalanceCronJob Started: {}", Instant.now());
        Iterable<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            EndingBalanceSnapshot endingBalance = new EndingBalanceSnapshot();
            endingBalance.setDateTime(Instant.now());
            endingBalance.setAccount(account);
            endingBalance.setBalance(account.getBalance());
            endingDailyBalanceRepository.save(endingBalance);
        }
        logger.info("endingDailyBalanceCronJob Completed: {}", Instant.now());
    }

    public Iterable<EndingBalanceSnapshot> getEndingBalanceByAccount(Account account) {
        Optional<Iterable<EndingBalanceSnapshot>> endingBalances = endingDailyBalanceRepository.findByAccount(account);
        if (!endingBalances.isPresent()) {
            return null;
        } else {
            return endingBalances.get();
        }
    }
}
