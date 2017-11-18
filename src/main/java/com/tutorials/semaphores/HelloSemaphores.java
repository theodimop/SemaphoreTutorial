package com.tutorials.semaphores;

import java.util.concurrent.Semaphore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by dj_di_000 on 18/11/2017.
 */
public class HelloSemaphores {

    public static final Logger logger = LogManager.getLogger(HelloSemaphores.class.getName());
    public static int produced = 0;
    public static int consumed = 0;
    public static int counter = 0;
    public static final Semaphore sem = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        startProducer();
        startConsumer();
    }

    private static void startConsumer() {
        new Thread(() -> {
            Worker consumer = new Worker();
            logger.info("Consumer initialized");


            while (true)
                try {

                    sem.acquire(); //Get semaphore

                    consumer.decrease();
                    consumed++;
                    logger.debug("Consume " + counter);
                    if (Math.abs(produced - consumed) != Math.abs(counter))
                        logger.fatal("C ->BOOOOOM Produced " + produced + " Consumed " + consumed + " balance" + counter);

                    sem.release(); //Release sem
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }).start();
    }

    private static void startProducer() {

        new Thread(() -> {
            Worker producer = new Worker();
            logger.info("Producer initialized");

            while (true)
                try {
                    sem.acquire();
                    producer.increase();
                    produced++;
                    logger.debug("Produce " + counter);

                    if (Math.abs(produced - consumed) != Math.abs(counter))
                        logger.fatal("P ->BOOOOOM Produced " + produced + " Consumed " + consumed + " balance " + counter);

                    sem.release();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }).start();
    }

    private static class Worker {
        void increase() {
            counter++;
        }

        void decrease() {
            counter--;
        }

        int get() {
            return counter;
        }
    }
}
