package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterDemo {
    private static final Logger logger = LoggerFactory.getLogger(CounterDemo.class);

    private String previousFlag = "second";

    public static void main(String[] args) {
        CounterDemo counterDemo = new CounterDemo();

        new Thread(() -> counterDemo.action(new Counter(), "first")).start();
        new Thread(() -> counterDemo.action(new Counter(), "second")).start();
    }

    private synchronized void action(Counter counter, String val) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (val.equals(previousFlag)) {
                    wait();
                }

                previousFlag = val;

                logger.info(String.valueOf(counter.getNextValue()));
                sleep();
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
