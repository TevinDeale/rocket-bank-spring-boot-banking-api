package com.tevind.rocketbank.controllers;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Customer;
import com.tevind.rocketbank.enums.AccountType;
import com.tevind.rocketbank.enums.TransactionType;
import com.tevind.rocketbank.service.AccountService;
import com.tevind.rocketbank.service.CustomerService;
import com.tevind.rocketbank.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@RestController
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController(AccountService service, CustomerService customerService, TransactionService transactionService) {
        this.accountService = service;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    @PostMapping("/api/account/add")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> payload) {
        logger.info("Running Account Add Control");
        String uid = getUUIDFromToken();

        Customer customer = customerService.getCustomer(uid);
        logger.info("Type passed through the request body: {}", payload.get("type"));
        String amount = payload.get("amount").toString();
        BigDecimal deposit = new BigDecimal(amount);
        Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setBalance(deposit);
        newAccount.setAccountType(AccountType.valueOf(payload.get("type").toString()));
        accountService.createAccount(newAccount);

        if(newAccount.getAccountNumber() == null || newAccount.getBalance() == null || newAccount.getAccountType() == null || newAccount.getCustomer() == null) {
            logger.warn("Error while creating account, a field was null ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String body = newAccount.getAccountType().getValue() + " Account: " + newAccount.getAccountNumber() + " created successfully!";
            logger.info(body);
            transactionService.createTransaction(newAccount, newAccount.getBalance(), TransactionType.CREDIT);
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/api/account/delete")
    public ResponseEntity<Account> deleteAccount(@RequestBody Map<String, Object> payload){
        logger.info("Running deleteAccount control");
        String uid = getUUIDFromToken();
        Account account = accountService.getAccountByAccountNumber(Integer.parseInt(payload.get("account_number").toString()));

        if(account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (!validateOwner(account, uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                Boolean success = accountService.deleteAccount(account);
                if (success) {
                    logger.info("Account {} closed", account.getAccountNumber());
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    logger.warn("Account not closed, Positive Balance");
                    return new ResponseEntity<>(account, HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

    @PutMapping("/api/account/deposit")
    public ResponseEntity<Account> deposit(@RequestBody Map<String, Object> payload) {
        logger.info("Running Deposit Control");
        String uid = getUUIDFromToken();
        Integer account_number = Integer.parseInt(payload.get("account_number").toString());
        BigDecimal depositAmount = new BigDecimal(payload.get("deposit").toString());
        Account account = accountService.getAccountByAccountNumber(account_number);

        if(account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if (!validateOwner(account, uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                Account updatedAccount = accountService.deposit(account, depositAmount);
                if (updatedAccount == null) {
                    logger.warn("Account Deposit unsuccessful");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else {
                    transactionService.createTransaction(account, depositAmount, TransactionType.CREDIT);
                    return new ResponseEntity<>(updatedAccount, HttpStatus.ACCEPTED);
                }
            }
        }
    }

    @PutMapping("/api/account/withdraw")
    public ResponseEntity<Account> withdraw(@RequestBody Map<String, Object> payload) {
        logger.info("Running Withdraw Control");
        String uid = getUUIDFromToken();
        Integer account_number = Integer.parseInt(payload.get("account_number").toString());
        BigDecimal withdrawAmount = new BigDecimal(payload.get("withdraw").toString());
        Account account = accountService.getAccountByAccountNumber(account_number);

        if(account == null) {
            logger.info("Account not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if(!validateOwner(account, uid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                Account updatedAccount = accountService.withdraw(account, withdrawAmount);
                if (updatedAccount == null) {
                    logger.warn("Account withdrawal unsuccessful");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                } else {
                    transactionService.createTransaction(account, withdrawAmount, TransactionType.DEBIT);
                    return new ResponseEntity<>(updatedAccount, HttpStatus.ACCEPTED);
                }
            }
        }
    }

    @PostMapping("/api/account")
    public ResponseEntity<Account> getAccount(@RequestBody Map<String, Object> payload) {
        Integer accountNumber = Integer.parseInt(payload.get("account_number").toString());
        logger.info("Staring to get account {}", accountNumber);
        String uuid = getUUIDFromToken();
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        logger.info("Looking for account ......");
        if (account == null) {
            logger.warn("Account does not Exist");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("Account {} was found. Validating Owner", accountNumber);
            if (!validateOwner(account, uuid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                logger.info("Return account to requester");
                return new ResponseEntity<>(account, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/api/account/getall")
    public ResponseEntity<Iterable<Account>> getAllAccounts() {
        String uuid = getUUIDFromToken();
        logger.info("Staring to get all accounts for {}", uuid);
        Iterable<Account> account = accountService.getAccounts(customerService.getCustomer(uuid));
        logger.info("Looking for accounts ......");
        if (account == null) {
            logger.warn("Customer has no open accounts");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("Accounts found");
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    public String getUUIDFromToken(){
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String uid = (String) authenticationToken.getToken().getClaims().get("sub");
        return uid;
    }

    public Boolean validateOwner(Account account, String uuid) {
        logger.info("Running validation checks");
        Boolean isValid = false;
        if(!Objects.equals(account.getCustomer().getUuid(), customerService.getCustomer(uuid).getUuid())) {
            logger.warn("Account Owner: {} does not match Auth: {}", account.getCustomer().getUuid(), uuid);
            return isValid;
        } else {
            logger.info("Token owner matches account owner");
            isValid = true;
            return isValid;
        }
    }
}
