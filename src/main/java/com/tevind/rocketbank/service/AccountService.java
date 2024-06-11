package com.tevind.rocketbank.service;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.Customer;
import com.tevind.rocketbank.enums.AccountStatus;
import com.tevind.rocketbank.enums.AccountType;
import com.tevind.rocketbank.repositories.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accounts;
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public AccountService(AccountRepository accounts){
        this.accounts = accounts;
    }

    @Autowired
    public EntityManager entityManager;

    @Transactional
    public Account createAccount(Account account) {
        logger.info("Account type to be created: {}", account.getAccountType());
        if(account.getAccountType() == AccountType.CHECKING) {
            Integer newVal = getNextSequenceValueChecking();
            account.setAccount_number(newVal);
            account.setAccountStatus(AccountStatus.OPEN);
            logger.info("Creating {} Account: {}", account.getAccountType(), account.getAccountNumber());
            logger.info("Initial Balance: {}", account.getBalance());
            logger.info("Account Owner: {}", account.getCustomer().getFullName());
            Account newAccount = accounts.save(account);
            return newAccount;
        } else if(account.getAccountType() == AccountType.SAVINGS) {
            Integer newVal = getNextSequenceValueSaving();
            account.setAccount_number(newVal);
            account.setAccountStatus(AccountStatus.OPEN);
            logger.info("Creating {} Account: {}", account.getAccountType(), account.getAccountNumber());
            logger.info("Initial Balance: {}", account.getBalance());
            logger.info("Account Owner: {}", account.getCustomer().getFullName());
            Account newAccount = accounts.save(account);
            return newAccount;
        } else {
            logger.warn("Account type cannot be NULL");
            System.err.println("Error creating account: Account Type cannot be NULL");
            return null;
        }
    }

    public Iterable<Account> getAccounts(Customer customer) {
        Optional<Iterable <Account>> optionalAccounts = accounts.findByCustomer(customer);
        logger.info("Finding Checking Account for {}", customer.getFullName());
        if (optionalAccounts.isPresent()) {
            logger.info("Accounts Found: {}", optionalAccounts.get());
            return optionalAccounts.get();
        } else {
            logger.info("No accounts found!");
            return null;
        }
    }

    public Account getAccountByAccountNumber(Integer account_number) {
        logger.info("Finding account by ID: {}", account_number);
        Optional<Account> account =  accounts.findById(account_number);

        if(account.isPresent()) {
            logger.info("Account Found");
            return account.get();
        } else {
            logger.info("Account Not Found");
            return null;
        }
    }

    @Transactional
    public Account deposit(Account account, BigDecimal amount) {
        logger.info("Deposit: ${}", amount);
        BigDecimal zero = new BigDecimal("0.00");
        if (amount.compareTo(zero) == 0) {
            logger.info("Running zero verification: ${} == ${} ${}",amount, zero, amount.compareTo(zero));
            return null;
        } else {
            BigDecimal newBalance = account.getBalance().add(amount);
            logger.info("NewBalance: ${}", newBalance);
            account.setBalance(newBalance);
            return accounts.save(account);
        }
    }

    @Transactional
    public Account withdraw(Account account, BigDecimal amount) {
        logger.info("Withdrawal: ${}", amount);
        BigDecimal zero = new BigDecimal(0);
        if (amount.compareTo(account.getBalance()) > 0){
            logger.info("Insufficient Funds!");
            return null;
        } else if (amount.compareTo(zero) == 0) {
            logger.info("amount is zero");
            return null;
        } else {
            BigDecimal newBalance = account.getBalance().subtract(amount);
            logger.info("NewBalance: ${}", newBalance);
            account.setBalance(newBalance);
            return accounts.save(account);
        }
    }

    @Transactional
    public Boolean deleteAccount(Account account) {
        logger.info("Deleting Account {}...", account.getAccountNumber());
        BigDecimal zero = new BigDecimal("0.00");
        Boolean success = false;

        BigDecimal balance = account.getBalance();
        if(balance.compareTo(zero) > 0) {
            logger.warn("Account still has a balance of ${}", balance);
            return success;
        } else {
            accounts.delete(account);
            logger.info("Account deleted successfully");
            success = true;
            return success;
        }
    }

    public Iterable<Account> findAll() {
        Iterable<Account> allAccounts = accounts.findAll();
        return allAccounts;
    }

    private Integer getNextSequenceValueChecking() {
        Query query = entityManager.createNativeQuery("SELECT nextval('\"account_seq\"')");
        return ((Number) query.getSingleResult()).intValue();
    }

    private Integer getNextSequenceValueSaving() {
        Query query = entityManager.createNativeQuery("SELECT nextval('\"savings_seq\"')");
        return ((Number) query.getSingleResult()).intValue();
    }
}
