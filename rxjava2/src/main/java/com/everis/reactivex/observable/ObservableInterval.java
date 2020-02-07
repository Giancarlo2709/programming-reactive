package com.everis.reactivex.observable;


import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class ObservableInterval {

    /*
    * Esto da Observable.interval () una oportunidad de disparar durante una
    * ventana de cinco segundos antes que la aplicación termine.
    */
    public static void main( String[] args ) {
        Observable<Long> secondIntervals = Observable.interval(1, TimeUnit.SECONDS);
        secondIntervals.subscribe(System.out::println);
        /* Mantenga el hilo principal durante 5 segundos para que se pueda disparar observable */
        sleep(5000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
