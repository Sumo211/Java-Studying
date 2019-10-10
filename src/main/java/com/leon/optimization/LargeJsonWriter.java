package com.leon.optimization;

public class LargeJsonWriter {

    //private ObjectMapper jsonMapper = new ObjectMapper();
    //private ExecutorService executorService = Executors.newFixedThreadPool(5);

    /*@Async
    public ListenableFuture<Boolean> export(UUID customerId) {
        try (PipedInputStream in = new PipedInputStream();
             PipedOutputStream pipedOut = new PipedOutputStream(in);
             GZIPOutputStream out = new GZIPOutputStream(pipedOut)) {

            Stopwatch stopwatch = Stopwatch.createStarted();

            ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();

            try (SequenceWriter sequenceWriter = writer.writeValues(out)) {
                sequenceWriter.init(true);

                Future<?> storageFuture = executorService.submit(() ->
                        storageProvider.storeFile(getFilePath(customerId), in));

                int batchCounter = 0;
                while (true) {
                    List<Record> batch = readDatabaseBatch(batchCounter++);
                    for (Record record : batch) {
                        sequenceWriter.write(entry);
                    }
                }

                // wait for storing to complete
                storageFuture.get();
            }

            logger.info("Exporting took {} seconds", stopwatch.stop().elapsed(TimeUnit.SECONDS));

            return AsyncResult.forValue(true);
        } catch (Exception ex) {
            logger.error("Failed to export data", ex);
            return AsyncResult.forValue(false);
        }
    }*/

}
