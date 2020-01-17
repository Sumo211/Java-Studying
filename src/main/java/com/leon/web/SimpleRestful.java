package com.leon.web;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class SimpleRestful {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleRestful.class);

    public static void main(String[] args) {
        Javalin.create(
                config -> config.defaultContentType = "application/json"
        ).routes(() -> path("metadata", () -> {
            get("phone/:userId", ctx -> {
                LOG.info("Getting identical number of {}", ctx.pathParam("userId"));
                sleep();
                ctx.json(generateRandomDigits.apply(10));
            });

            get("name/:userId", ctx -> {
                LOG.info("Getting alias of {}", ctx.pathParam("userId"));
                sleep();
                ctx.json(generateRandomString.apply(5));
            });
        })).start(9002);
    }

    private static void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private static Function<Integer, Long> generateRandomDigits = fixedLength -> {
        long base = (long) Math.pow(10, fixedLength - 1);
        return base + ThreadLocalRandom.current().nextLong(9 * base);
    };

    private static BiFunction<Integer, Integer, Character> generateRandomCharacter = (leftLimit, rightLimit) -> {
        int base = rightLimit - leftLimit + 1;
        int result = leftLimit + (int) (ThreadLocalRandom.current().nextFloat() * base);
        return (char) result;
    };

    private static Function<Integer, String> generateRandomString = fixedLength -> {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < fixedLength; i++) {
            buffer.append(generateRandomCharacter.apply(leftLimit, rightLimit));
        }

        return buffer.toString();
    };

}
