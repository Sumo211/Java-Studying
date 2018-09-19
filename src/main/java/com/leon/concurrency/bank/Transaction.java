package com.leon.concurrency.bank;

import java.math.BigDecimal;

class Transaction implements Runnable {

    private Bank bank;

    private int fromAccount;

    Transaction(Bank bank, int fromAccount) {
        this.bank = bank;
        this.fromAccount = fromAccount;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            int toAccount = (int) (Math.random() * Bank.MAX_ACCOUNT);
            if (toAccount == fromAccount) {
                continue;
            }

            BigDecimal amount = Bank.MAX_AMOUNT.multiply(BigDecimal.valueOf(Math.random()));
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            bank.transfer(fromAccount, toAccount, amount);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
