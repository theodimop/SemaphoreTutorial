package com.tutorials.countdownlatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;


/**
 * Created by dj_di_000 on 18/11/2017.
 */
public class HelloCountDownLatch {

    public static final Logger logger = LogManager.getLogger(HelloCountDownLatch.class.getName());
    public static final CountDownLatch startRaceLatch = new CountDownLatch(5);
    public static final CountDownLatch carsPreparedForRaceLatch = new CountDownLatch(3);


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(HelloCountDownLatch::prepareCarA);
        executor.execute(HelloCountDownLatch::prepareCarB);
        executor.execute(HelloCountDownLatch::prepareCarC);
        executor.execute(HelloCountDownLatch::startRace);
    }

    private static void startRace() {

        try {
            logger.info("Waiting for cars to prepare for the race.");
            carsPreparedForRaceLatch.await(); //Wait for the cars to prepare on the start line...

            while (startRaceLatch.getCount() > 0) {
                logger.info("Starting in " + startRaceLatch.getCount());
                Thread.sleep(1000);
                startRaceLatch.countDown();
            }

            startRaceLatch.countDown();
        } catch (InterruptedException e) {
            logger.error("Interrupted while performing countdown!");
        }


    }

    private static void prepareCarA() {
        new Thread(() -> {
            try {
                RaceCar carA = new RaceCar("A");
                carA.prepare();

                carsPreparedForRaceLatch.countDown();
                startRaceLatch.await();

                carA.go();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private static void prepareCarB() {
        new Thread(() -> {
            try {
                RaceCar carB = new RaceCar("B");
                carB.prepare();

                carsPreparedForRaceLatch.countDown();
                startRaceLatch.await();

                carB.go();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void prepareCarC() {
        new Thread(() -> {
            try {
                RaceCar carC = new RaceCar("C");
                carC.prepare();

                carsPreparedForRaceLatch.countDown();
                startRaceLatch.await();

                carC.go();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static class RaceCar {
        private String name;

        public RaceCar(String name) {
            this.name = name;
        }

        private void prepare() {
            logger.info(name + " :: Waiting for lights to go off...");
        }

        private void go() {
            logger.info(name + " :: Broooooommmmmmm!!!!");
        }
    }
}
