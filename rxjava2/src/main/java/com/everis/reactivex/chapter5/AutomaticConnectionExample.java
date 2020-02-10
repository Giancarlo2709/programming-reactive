package com.everis.reactivex.chapter5;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
Definitivamente, hay veces que querrá llamar manualmente a connect () en
Conectable Observable para controlar con precisión cuando las emisiones comienzan a disparar. Existen
operadores convenientes que automáticamente llaman a connect () para usted, pero con esta conveniencia,
Es importante tener conciencia de sus comportamientos de tiempo de suscripción. Permitiendo un
Observable para conectarse dinámicamente puede ser contraproducente si no tiene cuidado, ya que las emisiones pueden ser
extrañado por los observadores.
 */
public class AutomaticConnectionExample {

    public static void main(String[] args) {
        System.out.println("************** autoConnect **************");
        autoConnect();

        System.out.println("************** autoConnectOverflowSubscribers **************");
        autoConnectOverflowSubscribers();

        System.out.println("************** autoConnectInterval **************");
        autoConnectInterval();
    }

    /*
    * El operador autoConnect () en ConnectableObservable puede ser bastante útil. Para
    * dado ConnectableObservable <T>, llamar a autoConnect () devolverá un
    * <T> observable que llamará automáticamente a connect () después de un número específico de
    * Los observadores están suscritos. Como nuestro ejemplo anterior tenía dos observadores, podemos
    * racionalícelo llamando a autoConnect (2) inmediatamente después de publicar ():
     */
    private static void autoConnect() {
        Observable<Integer> threeRandoms = Observable.range(1, 3)
                .map(i -> randomInt())
                .publish()
                .autoConnect(2);

        //Observer 1 - print each random integer
        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

        //Observer 2 - sum the random integers, then print
        threeRandoms.reduce(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i));
    }

    private static int randomInt() {
        return ThreadLocalRandom.current().nextInt(100000);
    }

    /*
    * Incluso cuando todos los observadores aguas abajo terminen o eliminen, autoConnect () persistirá
    * suscripción a la fuente. Si la fuente es finita y dispone, no se volverá a suscribir
    * cuando un nuevo observador se suscribe aguas abajo. Si agregamos un tercer observador a nuestro
    * ejemplo, pero mantenga autoConnect () especificado en 2 en lugar de 3, es probable que el tercero
    * El observador va a perder las emisiones:
     */
    private static void autoConnectOverflowSubscribers() {
        Observable<Integer> threeRandoms = Observable.range(1, 3)
                .map(i -> randomInt())
                .publish()
                .autoConnect(2);

        //Observer 1 - print each random integer
        threeRandoms.subscribe(i -> System.out.println("Observer 1: " + i));

        //Observer 2 - sum the random integers, then print
        threeRandoms.reduce(0, (total, next) -> total + next)
                .subscribe(i -> System.out.println("Observer 2: " + i));

        //Observer 3 - receives nothing
        threeRandoms.subscribe(i -> System.out.println("Observer 3: " + i));
    }

    /*
    * Aquí, publicamos y autoConnect el Observable.interval (). El primer observador
    * comienza a disparar emisiones, y 3 segundos después, otro observador entra
    * pero pierde los primeros emisiones Pero recibe las emisiones vivas a partir de ese momento:
     */
    private static void autoConnectInterval() {
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();

        //Observer 1
        seconds.subscribe(i -> System.out.println("Observer 1: " + i));

        sleep(3000);

        //Observer 2
        seconds.subscribe(i -> System.out.println("Observer 2: "+ i));

        sleep(3000);
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
