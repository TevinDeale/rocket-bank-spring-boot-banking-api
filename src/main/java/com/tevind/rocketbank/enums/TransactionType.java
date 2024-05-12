package com.tevind.rocketbank.enums;

public enum TransactionType {
    DEBIT("debit"), CREDIT("credit");

    private final String value;

    TransactionType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
