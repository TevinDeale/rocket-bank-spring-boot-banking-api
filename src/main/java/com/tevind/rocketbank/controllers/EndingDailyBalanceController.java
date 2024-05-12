package com.tevind.rocketbank.controllers;

import com.tevind.rocketbank.entities.Account;
import com.tevind.rocketbank.entities.EndingBalanceSnapshot;
import com.tevind.rocketbank.service.AccountService;
import com.tevind.rocketbank.service.CustomerService;
import com.tevind.rocketbank.service.EndingDailyBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


@RestController
public class EndingDailyBalanceController {

    private final Logger logger = LoggerFactory.getLogger(EndingDailyBalanceController.class);
    private final EndingDailyBalanceService endingDailyBalanceService;
    private final AccountService accountService;
    private final CustomerService customerService;

    public EndingDailyBalanceController(EndingDailyBalanceService endingDailyBalanceService, AccountService accountService, CustomerService customerService) {
        this.endingDailyBalanceService = endingDailyBalanceService;
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @PostMapping("/api/account/history")
    public ResponseEntity<Iterable<EndingBalanceSnapshot>> getAccountEndingDailyBalance(@RequestBody Map<String, Object> payload) {
        Integer accountNumber = Integer.parseInt(payload.get("account_number").toString());
        logger.info("Getting account History for account {}", accountNumber);
        String uuid = getUUIDFromToken();
        Account account = accountService.getAccountByAccountNumber(accountNumber);

        logger.info("Checking to see if account exist");
        if(account == null) {
            logger.warn("Account {} does not exist", accountNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            if(!validateOwner(account, uuid)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else {
                Iterable<EndingBalanceSnapshot> endingBalances = endingDailyBalanceService.getEndingBalanceByAccount(account);
                logger.info("Checking for account history....");
                if (endingBalances == null) {
                    logger.warn("There is no account history for account {}", accountNumber);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    logger.info("Found account history for account {}", accountNumber);
                    return new ResponseEntity<>(endingBalances, HttpStatus.OK);
                }
            }
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
