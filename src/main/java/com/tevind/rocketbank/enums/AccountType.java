package com.tevind.rocketbank.enums;

public enum AccountType {
    CHECKING("Checking"), SAVINGS("Savings");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
