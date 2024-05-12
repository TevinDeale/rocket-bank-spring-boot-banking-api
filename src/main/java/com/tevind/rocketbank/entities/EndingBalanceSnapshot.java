package com.tevind.rocketbank.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "ending_daily_balance")
public class EndingBalanceSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_number", nullable = false)
    private Account account;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "date_time")
    private Instant dateTime;

    public Long getId() {
        return this.id;
    }

    public Account getAccount() {
        return this.account;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Instant getDateTime() {
        return this.dateTime;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }
}
