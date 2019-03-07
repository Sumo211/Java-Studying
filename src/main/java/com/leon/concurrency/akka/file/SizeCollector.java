package com.leon.concurrency.akka.file;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;

class SizeCollector extends AbstractActor {

    private final List<String> toProcessFileNames = new ArrayList<>();

    private final List<ActorRef> idleFileProcessors = new ArrayList<>();

    private long pendingNumberOfFilesToVisit = 0L;

    private long totalSize = 0L;

    private long start = System.nanoTime();

    static Props props() {
        return Props.create(SizeCollector.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RequestAFile.class, input -> {
                    idleFileProcessors.add(getContext().sender());
                    sendAFileToProcess();
                })
                .match(FileToProcess.class, input -> {
                    toProcessFileNames.add(input.getFileName());
                    pendingNumberOfFilesToVisit++;
                    sendAFileToProcess();
                })
                .match(FileSize.class, input -> {
                    totalSize += input.getSize();
                    pendingNumberOfFilesToVisit--;

                    if (pendingNumberOfFilesToVisit == 0) {
                        long end = System.nanoTime();
                        System.out.println("Total size is: " + totalSize);
                        System.out.println("Time taken is: " + (end - start) / 1.0e9);
                    }
                })
                .build();
    }

    private void sendAFileToProcess() {
        if (!toProcessFileNames.isEmpty() && !idleFileProcessors.isEmpty()) {
            idleFileProcessors.remove(0).tell(new FileToProcess(toProcessFileNames.remove(0)), getSelf());
        }
    }

    static class RequestAFile {

    }

    static class FileSize {

        private final long size;

        FileSize(long fileSize) {
            this.size = fileSize;
        }

        long getSize() {
            return size;
        }

    }

    static class FileToProcess {

        private final String fileName;

        FileToProcess(String fileName) {
            this.fileName = fileName;
        }

        String getFileName() {
            return fileName;
        }

    }

}
