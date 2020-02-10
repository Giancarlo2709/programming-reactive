package com.everis.reactivex.chapter4.combining.ambiguous;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/*
Después de cubrir la fusión y la concatenación, obtengamos una operación combinada fácil de
camino. La fábrica Observable.amb () (amb significa ambiguo) aceptará un
Iterable <Observable <T>> y emite las emisiones del primer Observable que emite,
mientras que los otros son eliminados. El primer observable con una emisión es aquel cuyo
las emisiones pasan. Esto es útil cuando tiene múltiples fuentes para los mismos datos o
eventos y quieres ganar el más rápido.
 */
public class AmbiguousExample {
    public static void main(String[] args) {

        //System.out.println("************** amb **************");
        //amb();

        System.out.println("************** ambWith **************");
        ambWith();
    }

    private static void amb() {
        //emit every second
        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .map(l -> l + 1)
                .map(l -> "Source1: " + l + " seconds");

        //emit every 300 milliseconds
        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300) // emit elapsed milliseconds
                .map(l -> "Source2: " + l + " milliseconds");

        //emit observable that emits first
        Observable.amb(Arrays.asList(source1, source2))
                .subscribe(i -> System.out.println("Received: " + i));

        //keep application alive for 5 seconds
        sleep(5000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void ambWith() {
        //emit every second
        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .map(l -> l + 1)
                .map(l -> "Source1: " + l + " seconds");

        //emit every 300 milliseconds
        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l + 1) * 300) // emit elapsed milliseconds
                .map(l -> "Source2: " + l + " milliseconds");

        source1.ambWith(source2)
                .subscribe(i -> System.out.println("Received: " + i));

        sleep(10000);
    }
}
