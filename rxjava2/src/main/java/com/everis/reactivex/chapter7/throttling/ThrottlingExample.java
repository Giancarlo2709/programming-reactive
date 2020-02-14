package com.everis.reactivex.chapter7.throttling;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class ThrottlingExample {

    public static void main(String[] args) {
        System.out.println("************** base **************");
        base();
    }

    /*
     * Para los ejemplos en esta sección, vamos a trabajar con este caso: tenemos tres
     * Fuentes observables.interval (), la primera emite cada 100 milisegundos, la segunda
     * cada 300 milisegundos, y el tercero cada 2000 milisegundos. Solo tomamos 10 emisiones
     * de la primera fuente, tres de la segunda y dos de la tercera. Como puedes ver aquí,
     * utilizaremos Observable.concat () en ellos juntos para crear una secuencia rápida
     * eso cambia el ritmo a tres intervalos diferentes:
     */
    private static void base() {
        Observable<String> source1 = Observable.interval(100, TimeUnit.MILLISECONDS)
                .map(i -> ( i + 1) * 100) //map to elapsed time
                .map(i -> "Source 1: " + i)
                .take(10);

        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) //map to elapsed time
                .map(i -> "Source 2: " + i)
                .take(3);

        Observable<String> source3 = Observable.interval(2000, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 2000) //map to elapsed time
                .map(i -> "Source 3: " + i)
                .take(2);

        Observable.concat(source1, source2, source3)
                .subscribe(System.out::println);

        ThreadUtil.sleep(6000);
    }
}
