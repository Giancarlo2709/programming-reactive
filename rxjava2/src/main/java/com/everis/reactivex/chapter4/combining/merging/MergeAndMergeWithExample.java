package com.everis.reactivex.chapter4.combining.merging;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
Para resumir, Observable.merge () combinará múltiples fuentes Observables <T>
emitiendo el mismo tipo T y consolidando en un solo Observable <T>. Funciona en infinito
Observables y no necesariamente garantiza que las emisiones se produzcan en ningún orden. Si
le importa que las emisiones estén estrictamente ordenadas al tener cada fuente Observable
disparado secuencialmente, es probable que desee utilizar Observable.concat ().
 */
public class MergeAndMergeWithExample {

    public static void main(String[] args) {

        System.out.println("****************** merge ******************");
        merge();

        System.out.println("****************** mergeWith ******************");
        mergeWith();

        System.out.println("****************** mergeArray ******************");
        mergeArray();

        System.out.println("****************** mergeWithIterable ******************");
        mergeWithIterable();

        System.out.println("****************** mergeWithInterval ******************");
        mergeWithInterval();

    }

    /*
    * El operador Observable.merge () tomará dos o más fuentes Observables <T>
    * emitiendo el mismo tipo T y luego consolidándolos en un solo Observable <T>.
    * Si solo tenemos dos o cuatro fuentes Observables <T> para fusionar, puede pasar cada una como un
    * argumento a la fábrica Observable.merge (). En el siguiente fragmento de código, tengo
    * fusionó dos instancias Observable <String> en una Observable <String>:
     */
    private static void merge() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        Observable.merge(source1, source2)
                .subscribe(i -> System.out.println("Received: " + i));

    }

    private static void mergeWith() {
        Observable<String> source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        Observable<String> source2 = Observable.just("Zeta", "Eta", "Theta");

        source1.mergeWith(source2)
                .subscribe(i -> System.out.println("Received: " + i));
    }

    /*
    * Si tiene más de cuatro fuentes Observables <T>, puede usar el
    * Observable.mergeArray () para pasar un varargs de instancias Observable [] que desee
    * fusionar, como se muestra en el siguiente fragmento de código
     */
    private static void mergeArray() {

        Observable<String> source1 = Observable.just("Alpha", "Beta");

        Observable<String> source2 = Observable.just("Gamma", "Delta");

        Observable<String> source3 = Observable.just("Epsilon", "Zeta");

        Observable<String> source4 = Observable.just("Eta", "Theta");

        Observable<String> source5 = Observable.just("Iota", "Kappa");

        Observable.mergeArray(source1, source2, source3, source4, source5)
                .subscribe(s-> System.out.println("Received: " + s));
    }

    /*
    * También puede pasar Iterable <Observable <T>> a Observable.merge (). Se fusionará
    * todas las instancias <T> observables en ese Iterable. Podría lograr el ejemplo anterior
    * de una forma más segura al escribir todas estas fuentes en la Lista <Observable <T>> y pasando
    * ellos a Observable.merge ():
     */
    private static void mergeWithIterable() {
        Observable<String> source1 = Observable.just("Alpha", "Beta");

        Observable<String> source2 = Observable.just("Gamma", "Delta");

        Observable<String> source3 = Observable.just("Epsilon", "Zeta");

        Observable<String> source4 = Observable.just("Eta", "Theta");

        Observable<String> source5 = Observable.just("Iota", "Kappa");

        List<Observable<String>> sources = Arrays.asList(source1, source2, source3, source4, source5);

        Observable.merge(sources)
                .subscribe(s-> System.out.println("Received: " + s));
    }

    /*
    * El Observable.merge () funciona con observables infinitos. Ya que se suscribirá a todos
    * Observables y disparar sus emisiones tan pronto como estén disponibles, puede combinar múltiples
    * fuentes infinitas en una sola secuencia. Aquí, fusionamos dos Observable.interval ()
    * fuentes que emiten a intervalos de un segundo y 300 milisegundos, respectivamente. Pero antes de nosotros
    * fusionar, hacemos algunos cálculos con el índice emitido para calcular cuánto tiempo ha transcurrido
    * y emitirlo con el nombre de la fuente en una cadena. Dejamos que este proceso se ejecute durante tres segundos:
     */
    private static void mergeWithInterval() {
        //emit every second
        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .map(l -> "Source1: " + l + " seconds");

        //emit every 300 milliseconds
        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> (l+1) * 300)
                .map(l -> "Source2: " + l + " milliseconds");

        //merge and subscribe
        Observable.merge(source1, source2)
                .subscribe(System.out::println);

        sleep(3000);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
