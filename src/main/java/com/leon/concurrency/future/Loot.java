package com.leon.concurrency.future;

import java.util.concurrent.ThreadLocalRandom;

public enum Loot {
    NICE("You've got 1000 Euro!"),
    NOT_BAD("You've got a limited edition figure of the Leia Princess"),
    BAD("Oh no, this is a trap! Police is coming.");

    private String message;

    Loot(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static Loot randomLoot() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return Loot.values()[ThreadLocalRandom.current().nextInt(0, 3)];
    }

}
