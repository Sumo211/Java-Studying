package com.leon.validation;

import lombok.Value;

import java.util.Objects;

public class TestApp {

    public static void main(String[] args) {
        User user = new User("user", 24, "foobar.com");
        System.out.println(Validator.of(user)
                .validate(User::getName, Objects::nonNull, "Name is null")
                .validate(User::getName, name -> !name.isEmpty(), "Name is empty")
                .validate(User::getEmail, email -> email.contains("@"), "Email format is incorrect")
                .validate(User::getAge, age -> age > 20 && age < 30, "Age isn't between specified range")
                .get().toString());
    }

    @Value
    private static class User {

        private final String name;

        private final int age;

        private final String email;

    }

}
