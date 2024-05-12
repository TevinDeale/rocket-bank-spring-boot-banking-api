package com.tevind.rocketbank.entities;

import com.tevind.rocketbank.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="transactions")
public class Transaction {

    @Id
    private Integer transaction_id;

    @ManyToOne
    @JoinColumn(name="account_number", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type", nullable = false)
    private TransactionType type;

    @Column(name="transaction_amount", nullable = false)
    private BigDecimal amount;

    @Column(name="transaction_date", nullable = false)
    private Instant dateTime;

    public Integer getTransaction_id() {
        return this.transaction_id;
    }

    public TransactionType getType() {
        return this.type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Instant getDateTime() {
        return this.dateTime;
    }

    public void setTransaction_id(Integer transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }
}
