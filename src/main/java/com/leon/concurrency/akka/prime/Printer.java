package com.leon.concurrency.akka.prime;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Printer extends AbstractActor {

    static Props props() {
        return Props.create(Printer.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, result -> System.out.println("The final result is: " + result))
                .matchAny(o -> System.out.println("Receive unknown message"))
                .build();
    }

}
