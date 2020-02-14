package com.everis.reactivex.util;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadUtil {

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> T intenseCalculation(T value) {
        //sleep up to 200 milliseconds
        sleep(ThreadLocalRandom.current().nextInt((Integer) value));
        return value;
    }
}
