package com.everis.reactivex.chapter7.throttling;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
El operador throttleLast () (que tiene el alias como sample()) solo emitirá el último elemento
en un intervalo de tiempo fijo. Modifique su ejemplo anterior para usar throttleLast () en 1 segundo
intervalos, como se muestra aquí:
 */
public class ThrottleFirstExample {

    public static void main(String[] args) {

        /*System.out.println("**************** base ****************");
        base();*/

        System.out.println("**************** throtteLastWith2Seconds ****************");
        throttleFirstWith2Seconds();

        ThreadUtil.sleep(6000);
    }

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
                .throttleLast(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }

    /*
     * Efectivamente, la primera emisión encontrada después de que comienza cada intervalo es la emisión que obtiene
     * empujado a través. El 100 de la fuente1 fue la primera emisión encontrada en el primer intervalo.
     * En el siguiente intervalo, se emitieron 300 de source2, luego 2000, seguido de 4000. El 4000
     * se emitió justo en la cúspide de la aplicación al salir, por lo tanto, obtuvimos cuatro emisiones de
     * throttleFirst () en lugar de tres de throttleLast ().
     * Además del primer elemento que se emite en lugar del último en cada intervalo, todos los comportamientos
     * from throttleLast () también se aplica a throttleFirst (). Especificar intervalos más cortos
     * producir más emisiones, mientras que intervalos más largos producirán menos.
     * Tanto throttleFirst () como throttleLast () emiten en el Programador de cálculo, pero
     * Puede especificar su propio Programador como tercer argumento.
     */
    private static void throttleFirstWith2Seconds() {
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
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(System.out::println);
    }
}
