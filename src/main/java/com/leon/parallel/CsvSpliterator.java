package com.leon.parallel;

import java.io.BufferedReader;
import java.util.function.Consumer;

public class CsvSpliterator extends FixedBatchSpliteratorBase<String[]> {

    private final BufferedReader reader;

    @SuppressWarnings("WeakerAccess")
    public CsvSpliterator(BufferedReader reader, int batchSize) {
        super(IMMUTABLE | NONNULL | ORDERED, batchSize);
        if (reader == null) {
            throw new NullPointerException("Reader is null");
        }

        this.reader = reader;
    }

    @SuppressWarnings("unused")
    public CsvSpliterator(BufferedReader reader) {
        this(reader, 128);
    }

    @Override
    public void forEachRemaining(Consumer<? super String[]> action) {
        if (action == null) {
            throw new NullPointerException();
        }

        try {
            for (String line; (line = reader.readLine()) != null; ) {
                action.accept(line.split(","));
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean tryAdvance(Consumer<? super String[]> action) {
        if (action == null) {
            throw new NullPointerException();
        }

        try {
            final String line = reader.readLine();
            if (line == null) {
                return false;
            }

            action.accept(line.split(","));
            return true;
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
