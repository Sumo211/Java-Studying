package com.leon.concurrency.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class Action {

    private static final Long DELAY_MS = 1000L;

    private Action() {

    }

    public static boolean unlockTheDoor() {
        log.info("Forcing the door...");
        delay(2000);
        log.info("Door unlocked!");
        return true;
    }

    public static int hackSecretPin(final String name) {
        log.info("Hacking the pin of {}", name);
        delay();
        final int pin = (name.hashCode() % 1000) + 1000;
        log.info("Pin hacked: {}", pin);
        return pin;
    }

    public static String figureOutSafetyBoxNumber(final String name) {
        log.info("Figuring out the safety box number of {}", name);
        delay();
        final String lock = "A" + ThreadLocalRandom.current().nextInt(100);
        log.info("Got the safety box number: {}", lock);
        return lock;
    }

    public static Loot openSafeLock(final int pin, final String safetyBoxNumber) {
        log.info("Opening the safe lock {} using the pin {}", safetyBoxNumber, pin);
        delay();
        log.info("Safety Box opened!");
        return Loot.randomLoot();
    }

    private static void delay() {
        delay(DELAY_MS);
    }

    private static void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

}
