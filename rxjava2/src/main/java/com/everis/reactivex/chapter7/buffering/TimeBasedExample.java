package com.everis.reactivex.chapter7.buffering;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
Puede usar buffer () a intervalos de tiempo fijos proporcionando una unidad de tiempo larga y unitaria. A
las emisiones del búfer en una lista a intervalos de 1 segundo, puede ejecutar el siguiente código. Tenga en cuenta que
estamos haciendo que la fuente emita cada 300 milisegundos, y cada lista almacenada resultante
probablemente contenga tres o cuatro emisiones debido a los cortes de intervalo de un segundo:
 */
public class TimeBasedExample {

    public static void main(String[] args) {
        System.out.println("************* base *************");
        base();

        ThreadUtil.sleep(4000);
    }

    private static void base() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(i -> (i + 1) * 300) // map to elapsed time
                .buffer(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }
}
