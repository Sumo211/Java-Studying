package com.leon.concurrency.akka.file;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Status;

import java.io.File;

class FileProcessor extends AbstractActor {

    private final ActorRef sizeCollector;

    public FileProcessor(ActorRef sizeCollector) {
        this.sizeCollector = sizeCollector;
    }

    static Props props(ActorRef sizeCollector) {
        return Props.create(FileProcessor.class, sizeCollector);
    }

    @Override
    public void preStart() {
        registerToGetFile();
    }

    private void registerToGetFile() {
        sizeCollector.tell(new SizeCollector.RequestAFile(), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SizeCollector.FileToProcess.class, input -> {
                    try {
                        final File file = new File(input.getFileName());
                        long size = 0L;

                        if (file.isFile()) {
                            size = file.length();
                        } else {
                            File[] children = file.listFiles();
                            if (children != null) {
                                for (File child : children) {
                                    if (child.isFile()) {
                                        size += child.length();
                                    } else {
                                        sizeCollector.tell(new SizeCollector.FileToProcess(child.getPath()), getSelf());
                                    }
                                }
                            }
                        }

                        sizeCollector.tell(new SizeCollector.FileSize(size), getSelf());
                        registerToGetFile();
                    } catch (Exception ex) {
                        getSender().tell(new Status.Failure(ex), getSelf());
                        throw ex;
                    }
                })
                .build();
    }

}
