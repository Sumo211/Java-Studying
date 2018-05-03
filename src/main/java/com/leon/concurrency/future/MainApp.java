package com.leon.concurrency.future;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainApp {

    public static void main(String[] args) {
        log.info("\n\n COMPLETABLE FUTURE ====");
        final Loot completableFutureLoot = new CompletableFutureOpenSafeLock().openSafeLock(Thief.PETE, "Sr. Carapapas");
        log.info("App got the loot {}", completableFutureLoot);
    }

}
