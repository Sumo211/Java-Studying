package com.leon.concurrency.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFutureOpenSafeLock {

    public Loot openSafeLock(final Thief thief, final String victim) {
        return CompletableFuture.supplyAsync(Action::unlockTheDoor)
                .thenCompose(isOpened ->
                    CompletableFuture.supplyAsync(() -> Action.hackSecretPin(victim))
                        .thenCombineAsync(
                            CompletableFuture.supplyAsync(() -> Action.figureOutSafetyBoxNumber(victim)),
                            Action::openSafeLock
                        ).exceptionally(ex -> {
                            log.error("Something went wrong: {} Run, run, run!!", ex.getMessage());
                            return Loot.BAD;
                        })
                ).thenApply(
                    loot -> {
                        log.info("{} gets the content of the safety box: '{}'", thief.getName(), thief.handleLoot(loot));
                        return loot;
                    }
                ).join();
    }

}
