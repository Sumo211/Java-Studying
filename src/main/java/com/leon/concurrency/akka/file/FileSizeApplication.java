package com.leon.concurrency.akka.file;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class FileSizeApplication {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("FAIL");

        final ActorRef sizeCollector = system.actorOf(SizeCollector.props());
        sizeCollector.tell(new SizeCollector.FileToProcess("C:\\DEV"), ActorRef.noSender());

        for (int i = 0; i < 100; i++) {
            system.actorOf(FileProcessor.props(sizeCollector));
        }
    }

}
