package com.leon.concurrency.akka.prime;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;

public class Primes extends AbstractActor {

    private final String name;

    static Props props(String name) {
        return Props.create(Primes.class, () -> new Primes(name));
    }

    private Primes(String name) {
        this.name = name;
    }

    @Override
    public void preStart() {
        //System.out.println("Pre-start from Thread: " + Thread.currentThread().getName());
    }

    @Override
    public void postStop() {
        //System.out.println("Post-stop from Thread: " + Thread.currentThread().getName());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RangeInput.class, input -> {
                    try {
                        System.out.println(name + " is counting...");
                        getSender().tell(PrimeFinder.countPrimesInRange(input.getLower(), input.getUpper()), getSelf());
                    } catch (Exception ex) {
                        getSender().tell(new Status.Failure(ex), getSelf());
                        throw ex;
                    }
                })
                .build();
    }

    static class RangeInput {

        private final int lower;

        private final int upper;

        RangeInput(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }

        int getLower() {
            return lower;
        }

        int getUpper() {
            return upper;
        }

    }

}
