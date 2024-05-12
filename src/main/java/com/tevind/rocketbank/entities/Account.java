package com.tevind.rocketbank.entities;

import com.tevind.rocketbank.enums.AccountStatus;
import com.tevind.rocketbank.enums.AccountType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    private Integer account_number;

    @Column(name="balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name="account_type", nullable = false)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    @Column(name="account_status", nullable = false)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    public Integer getAccountNumber() {
        return this.account_number;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public AccountType getAccountType() {
        return this.type;
    }

    public AccountStatus getAccountStatus() {
        return this.status;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAccount_number(Integer account_number) {
        this.account_number = account_number;
    }

    public void setAccountType(AccountType type) {
        this.type = type;
    }

    public void setAccountStatus(AccountStatus status) {
        this.status = status;
    }
}
