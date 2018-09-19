package com.leon.concurrency.bank;

public class Application {

    public static void main(String[] args) {
        Bank bank = new Bank();
        for (int i = 0; i < Bank.MAX_ACCOUNT; i++) {
            Thread transaction = new Thread(new Transaction(bank, i));
            transaction.start();
        }
    }

}
