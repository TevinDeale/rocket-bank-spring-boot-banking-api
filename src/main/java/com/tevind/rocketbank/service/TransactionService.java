package com.tevind.rocketbank.service;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Transaction;
import com.tevind.rocketbank.enums.TransactionType;
import com.tevind.rocketbank.repositories.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactions;
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(TransactionRepository transactions){
        this.transactions = transactions;
    }

    @Autowired
    public EntityManager entityManager;

    public Transaction createTransaction(Account account, BigDecimal amount, TransactionType type) {
        logger.info("Saving Transaction");
        Transaction transaction = new Transaction();
        transaction.setTransaction_id(getNextSequenceValueTransaction());
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDateTime(Instant.now());
        logger.info(transaction.getTransaction_id().toString());
        transactions.save(transaction);
        logger.info("Transaction Saved");
        return transaction;
    }

    public Page<Transaction> getTransactionsPaged(Account account, Pageable pageable) {
        logger.info("Getting transactions for {}", account.getAccountNumber());
        Optional<Page<Transaction>> optionalTransactions = transactions.findAllByAccount(account, pageable);
        if(optionalTransactions.isPresent()) {
            logger.info("Transactions Found");
            return optionalTransactions.get();
        } else {
            logger.info("No transactions found");
            return null;
        }
    }

    public List<Transaction> getTransactions(Account account) {
        logger.info("Getting all transactions for {}", account.getAccountNumber());
        Optional<List<Transaction>> optionalTransactions = transactions.findAllByAccount(account);
        if(optionalTransactions.isPresent()) {
            logger.info("Transactions Found");
            return optionalTransactions.get();
        } else {
            logger.info("No transactions found");
            return null;
        }
    }

    private Integer getNextSequenceValueTransaction() {
        Query query = entityManager.createNativeQuery("SELECT nextval('\"transaction_seq\"')");
        return ((Number) query.getSingleResult()).intValue();
    }
}
