package com.everis.reactivex.chapter7.throttling;

import com.everis.reactivex.util.ThreadUtil;
import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

/*
El operador throttleLast () (que tiene el alias como sample()) solo emitirá el último elemento
en un intervalo de tiempo fijo. Modifique su ejemplo anterior para usar throttleLast () en 1 segundo
intervalos, como se muestra aquí:
 */
public class ThrottleLastExample {

    public static void main(String[] args) {

        /*System.out.println("**************** base ****************");
        base();*/

        System.out.println("**************** throtteLastWith2Seconds ****************");
        throtteLastWith2Seconds();

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
     * Si estudia la salida, puede ver que la última emisión en cada intervalo de 1 segundo fue todo
     * eso pasó. Esto efectivamente muestra las emisiones al sumergirse en la corriente en un temporizador
     * y sacando la última.
     * Si desea acelerar más generosamente a intervalos de tiempo más largos, obtendrá menos emisiones
     * ya que esto reduce efectivamente la frecuencia de muestreo. Aquí, usamos throttleLast () cada dos
     * segundos:
     */
    private static void throtteLastWith2Seconds() {
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
                .throttleLast(500, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);
    }
}
