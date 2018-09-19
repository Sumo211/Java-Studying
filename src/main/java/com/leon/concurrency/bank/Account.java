package com.leon.concurrency.bank;

import java.math.BigDecimal;

class Account {

    private BigDecimal balance;

    Account(BigDecimal balance) {
        this.balance = balance;
    }

    void withdrawn(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    BigDecimal getBalance() {
        return this.balance;
    }

}
