package com.leon.concurrency.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank {

    private static final Logger LOG = LoggerFactory.getLogger(Bank.class);

    static final int MAX_ACCOUNT = 10;

    static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(10);

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100);

    private Account[] accounts = new Account[MAX_ACCOUNT];

    private Lock bankLock;

    private Condition availableFund;

    Bank() {
        for (int i = 0; i < MAX_ACCOUNT; i++) {
            accounts[i] = new Account(INITIAL_BALANCE);
        }

        bankLock = new ReentrantLock();
        availableFund = bankLock.newCondition();
    }

    void transfer(int from, int to, BigDecimal amount) {
        bankLock.lock();
        try {
            while (amount.compareTo(accounts[from].getBalance()) > 0) {
                availableFund.await();
            }

            accounts[from].withdrawn(amount);
            accounts[to].deposit(amount);
            LOG.info("{} transferred {} from {} to {}. Total balance: {}", Thread.currentThread().getName(), amount.intValue(), from, to, getTotalBalance().intValue());
            availableFund.signalAll();
        } catch (InterruptedException ex) {
            LOG.error("Error: " + ex.getCause());
        } finally {
            bankLock.unlock();
        }
    }

    private BigDecimal getTotalBalance() {
        bankLock.lock();
        try {
            BigDecimal total = BigDecimal.ZERO;
            for (int i = 0; i < MAX_ACCOUNT; i++) {
                total = total.add(accounts[i].getBalance());
            }

            return total;
        } finally {
            bankLock.unlock();
        }
    }

}
