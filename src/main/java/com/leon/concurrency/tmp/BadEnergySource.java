package com.leon.concurrency.tmp;

public class BadEnergySource {

    private final long MAX_LEVEL = 100;

    private long level = MAX_LEVEL;

    private boolean keepRunning = true;

    public BadEnergySource() {
        new Thread(this::replenish).start();
    }

    public long getUnitsAvailable() {
        return level;
    }

    public boolean useEnergy(long units) {
        if (units > 0 && level >= units) {
            level -= units;
            return true;
        }

        return false;
    }

    public void stopEnergySource() {
        keepRunning = false;
    }

    private void replenish() {
        while (keepRunning) {
            if (level < MAX_LEVEL) {
                level++;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.err.println("Error: " + ex);
            }
        }
    }

}
