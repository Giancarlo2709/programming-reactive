package com.everis.reactivex.chapter6.concurrency.schedulers;

import com.everis.reactivex.util.ObservableUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/*
Ya hemos mencionado el uso de subscribeOn (), pero en esta sección, lo exploraremos
con más detalle y mira cómo funciona.
El operador subscribeOn () sugerirá a la fuente Observable aguas arriba, que
Programador para usar y cómo ejecutar operaciones en uno de sus hilos. Si esa fuente no es
ya vinculado a un Programador particular, usará el Programador que especifique. Entonces lo hará
empujar las emisiones hasta el observador final usando ese hilo (a menos que agregue
Observe las llamadas (), que cubriremos más adelante). Puede poner subscribeOn () en cualquier lugar de
la cadena Observable, y sugerirá a la corriente arriba hasta el origen
Observable con qué hilo ejecutar emisiones.
 */
public class UnderstandingSubscribeOnExample {

    /*
    * En el siguiente ejemplo, no importa si coloca este subscribeOn ()
    * justo después de Observable.just () o después de uno de los operadores. SubscribeOn ()
    * comunicarse en sentido ascendente con el Observable.just () qué Programador usar sin importar
    * donde lo pones Sin embargo, para mayor claridad, debe colocarlo lo más cerca posible de la fuente:
     */
    public static void main(String[] args) {

        /*System.out.println("*************** observeOnExample ***************");
        observeOnExample();

        System.out.println("*************** oneThread ***************");
        oneThread();

        System.out.println("*************** fromCallable ***************");
        fromCallable();

        System.out.println("*************** newThread ***************");
        newThread();*/

        System.out.println("*************** computationVsIO ***************");
        computationVsIO();

        sleep(10000);
    }

    private static void observeOnExample() {
        Observable<Integer> lengths = ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .map(UnderstandingSubscribeOnExample::intenseCalculation)
                .map(String::length);

        lengths.subscribe(i ->
                System.out.println("Received: " + i + " on thread " +
                        Thread.currentThread().getName()));

        lengths.subscribe(i ->
                System.out.println("Received: " + i + " on thread " +
                        Thread.currentThread().getName()));
    }

    public static <T> T intenseCalculation(T value) {
        sleep(ThreadLocalRandom.current().nextInt(3000));
        return value;
    }
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
    * Observe cómo un observador está utilizando un hilo llamado RxComputationThreadPool-2, mientras
    * el otro está usando RxComputationThreadPool-1. Estos nombres indican qué planificador
    * vinieron de (que es la Computación) y cuál es su índice. Como se muestra aquí, si
    * queremos solo un hilo para servir a ambos Observadores, podemos multidifundir esta operación. Sólo
    * asegúrese de que subscribeOn () esté antes de los operadores de multidifusión:
     */
    private static void oneThread() {
        Observable<Integer> lenghts = ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .map(UnderstandingSubscribeOnExample::intenseCalculation)
                .map(String::length)
                .publish()
                .autoConnect(2);

        lenghts.subscribe(i -> System.out.println("Received: " + i + " on thread " +
                Thread.currentThread().getName()));

        lenghts.subscribe(i -> System.out.println("Received: " + i + " on thread " +
                Thread.currentThread().getName()));
    }

    /*
    La mayoría de las fábricas observables, como Observable.fromIterable () y
    * Observable.just (), emitirá elementos en el Programador especificado por subscribeOn (). por
    * fábricas como Observable.fromCallable () y Observable.defer (), el
    * La inicialización de estas fuentes también se ejecutará en el Programador especificado cuando se utiliza
    * subscribeOn (). Por ejemplo, si usa Observable.fromCallable () para esperar en una URL
    * respuesta, en realidad puedes hacer ese trabajo en el IO Scheduler para que el hilo principal no sea
    * bloqueo y espera de ello:
     */
    private static void fromCallable() {
        Observable.fromCallable(() ->
                getResponse("https://api.github.com/users/thomasnield/starred")
        ).subscribeOn(Schedulers.io())
                .subscribe(System.out::println);
    }

    private static String getResponse(String path) {
        try {
            return new Scanner(new URL(path).openStream(),
                    "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /*
    * Es importante tener en cuenta que subscribeOn () no tendrá ningún efecto práctico con ciertas
    * fuentes (y mantendrá un subproceso de trabajo innecesariamente en espera hasta esa operación
    * termina). Esto podría deberse a que estos Observables ya usan un Programador específico,
    * y si desea cambiarlo, puede proporcionar un Programador como argumento. por
    * ejemplo, Observable.interval () usará Schedulers.computation () y
    * ignore cualquier subscribeOn () que especifique de otra manera. Pero puedes proporcionar un tercer argumento para
    * especifique un programador diferente para usar. Aquí, especifico Observable.interval () para
    * use Schedulers.newThread (), como se muestra aquí:
     */
    private static void newThread() {
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.newThread())
                .subscribe(i -> System.out.println("Received: " + i + " on thread " +
                        Thread.currentThread().getName()));


    }

    /*
    * Esto también trae otro punto: si tiene múltiples llamadas subscribeOn () en un determinado
    * La cadena observable, la más alta o la más cercana a la fuente, ganará y causará
    * los posteriores no tendrán efecto práctico (aparte del uso innecesario de recursos). Si yo
    * llame a subscribeOn () con Schedulers.computation () y luego llame a subscribeOn () para
    * Schedulers.io (), Schedulers.computation () es el que se utilizará:
     */
    private static void computationVsIO() {
        ObservableUtil.GREEK_ALPHABET
                .subscribeOn(Schedulers.computation())
                .filter(s -> s.length() == 5)
                .subscribeOn(Schedulers.io())
                .subscribe(i -> System.out.println("Received: " + i + " on thread " +
                        Thread.currentThread().getName()));
    }
}
