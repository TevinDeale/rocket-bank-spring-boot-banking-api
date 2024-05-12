package com.tevind.rocketbank.controllers;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Transaction;
import com.tevind.rocketbank.service.AccountService;
import com.tevind.rocketbank.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping("/api/account/transactions")
    public ResponseEntity<Page<Transaction>> getSomeTransactions(@RequestBody Map<String, Object> payload,
                                                            @RequestParam(defaultValue = "12") int size,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "desc") String sort,
                                                            @RequestParam(defaultValue = "dateTime") String sortBy) {
        logger.info("Starting the Transactions Controller");
        System.out.println(payload);
        Integer account_number = Integer.parseInt(payload.get("account_number").toString());
        Account account = accountService.getAccountByAccountNumber(account_number);
        logger.info("Getting all Transactions for account: {}", account.getAccountNumber());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sort), sortBy);
        Page<Transaction> transactions = transactionService.getTransactionsPaged(account, pageRequest);

        if(transactions != null) {
            logger.info("Transactions Found");
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } else {
            logger.info("No transactions found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/account/transactions/getall")
    public ResponseEntity<List<Transaction>> getSomeTransactions(@RequestBody Map<String, Object> payload) {
        logger.info("Starting the Transactions Get All Controller");
        System.out.println(payload);
        Integer account_number = Integer.parseInt(payload.get("account_number").toString());
        Account account = accountService.getAccountByAccountNumber(account_number);
        logger.info("Getting all Transactions for account: {}", account.getAccountNumber());
        List<Transaction> transactions = transactionService.getTransactions(account);

        if(transactions != null) {
            logger.info("Transactions Found");
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } else {
            logger.info("No transactions found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
